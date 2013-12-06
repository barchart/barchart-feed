/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.thread;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.common.anno.ThreadSafe;

/**
 * use this thread implementation to enable InterruptedException - like behavior
 * for IO methods that block on input/output but ignore Thread.interrupt() call;
 * such as DatagramSocket.receive(), InputStream.read(), etc.; thread target
 * must be of a supported type : {@link java.net.ServerSocket},
 * {@link java.net.Socket}, {@link java.net.DatagramSocket},
 * {@link java.io.InputStream}, {@link java.io.OutputStream},
 * {@link java.io.Reader}, {@link java.io.Writer};
 */
@ThreadSafe
public class ThreadInterruptibleIO extends Thread {

	/** target wrapper */
	private interface Interrupter {
		/** one time action called on ThreadInterruptibleIO.interrupt */
		void fire();
	}

	private final static Logger log = LoggerFactory
			.getLogger(ThreadInterruptibleIO.class);

	/** guarded by "this"; one list per thread */
	private final List<Interrupter> interrupterList = new LinkedList<Interrupter>();

	public ThreadInterruptibleIO(final Runnable task) {
		super(task);
	}

	/** target must be of supported type */
	private final void registerTarget(final Object target) {

		final Interrupter interrupter;

		if (target == null) {
			log.error("null target; ignoring");
			return;
		} else if (target instanceof ServerSocket) {
			interrupter = new Interrupter() {

				final ServerSocket socket = (ServerSocket) target;

				@Override
				public void fire() {
					try {
						socket.close();
					} catch (final Exception e) {
						log.error("unexpected; ignoring", e);
					}
				}

				@Override
				public String toString() {
					return socket.toString();
				}
			};
		} else if (target instanceof Socket) {
			interrupter = new Interrupter() {

				final Socket socket = (Socket) target;

				@Override
				public void fire() {
					try {
						socket.close();
					} catch (final Exception e) {
						log.error("unexpected; ignoring", e);
					}
				}

				@Override
				public String toString() {
					return socket.toString();
				}
			};
		} else if (target instanceof DatagramSocket) {
			interrupter = new Interrupter() {

				final DatagramSocket socket = (DatagramSocket) target;

				@Override
				public void fire() {
					socket.close();
				}

				@Override
				public String toString() {
					return "DatagramSocket[ local=" + socket.getLocalAddress()
							+ " remote=" + socket.getInetAddress() + " ]";
				}
			};
		} else if (target instanceof InputStream) {
			interrupter = new Interrupter() {

				final InputStream stream = (InputStream) target;

				@Override
				public void fire() {
					try {
						stream.close();
					} catch (final Exception e) {
						log.error("unexpected; ignoring", e);
					}
				}

				@Override
				public String toString() {
					return stream.toString();
				}
			};
		} else if (target instanceof OutputStream) {
			interrupter = new Interrupter() {

				final OutputStream stream = (OutputStream) target;

				@Override
				public void fire() {
					try {
						stream.close();
					} catch (final Exception e) {
						log.error("unexpected; ignoring", e);
					}
				}

				@Override
				public String toString() {
					return stream.toString();
				}
			};
		} else if (target instanceof Reader) {
			interrupter = new Interrupter() {

				final Reader stream = (Reader) target;

				@Override
				public void fire() {
					try {
						stream.close();
					} catch (final Exception e) {
						log.error("unexpected; ignoring", e);
					}
				}

				@Override
				public String toString() {
					return stream.toString();
				}
			};
		} else if (target instanceof Writer) {
			interrupter = new Interrupter() {

				final Writer stream = (Writer) target;

				@Override
				public void fire() {
					try {
						stream.close();
					} catch (final Exception e) {
						log.error("unexpected; ignoring", e);
					}
				}

				@Override
				public String toString() {
					return stream.toString();
				}
			};
		} else {

			log.error("unsupported target type; ignoring; class : {}", target
					.getClass().getName());
			return;

		}

		synchronized (interrupterList) {
			if (interrupterList.contains(target)) {
				log.error("trying to add duplicate target; ignoring");
				return;
			}
			interrupterList.add(interrupter);
		}

		log.debug("added interrupter : {}", interrupter);

	}

	/**
	 * will clear interrupted targets from registration list after the interrupt
	 * event
	 */
	@Override
	public final void interrupt() {

		// propagate interrupt
		super.interrupt();

		synchronized (interrupterList) {
			for (final Interrupter interrupter : interrupterList) {
				interrupter.fire();
				log.debug("fired interrupter : {}", interrupter);
			}
			interrupterList.clear();
		}

	}

	/**
	 * target must be of supported type
	 */
	public final void addTarget(final Object target) {
		registerTarget(target);
	}

	/**
	 * note that interrupt event will auto clear all thread targets; call this
	 * if you want to disable target before the interrupt occurred
	 */
	public final void removeTarget(final Object target) {
		unregisterTarget(target);
	}

	/**
	 * must be called from ThreadInterruptibleIO thread; target must be of
	 * supported type;
	 */
	public static final void addTargetToCurrentThread(final Object target) {

		final Thread currentThread = Thread.currentThread();

		if (currentThread instanceof ThreadInterruptibleIO) {

			final ThreadInterruptibleIO interruptableThread = //
			(ThreadInterruptibleIO) currentThread;

			interruptableThread.addTarget(target);

		} else {
			log.error("unsupported operation; ignoring; thread class : {}",
					currentThread.getClass().getName());
		}

	}

	private static final AtomicInteger factoryThreadCount = new AtomicInteger(0);

	private static final ThreadFactory factory = new ThreadFactory() {
		@Override
		public Thread newThread(final Runnable task) {
			final Thread thread = new ThreadInterruptibleIO(task);
			thread.setName("InterruptibleThread-"
					+ factoryThreadCount.getAndIncrement());
			return thread;
		}
	};

	/**
	 * executors must use this factory, so that tasks can add their
	 * interruptible IO targets;
	 */
	public static final ThreadFactory getFactory() {
		return factory;
	}

	/**
	 * must be called from ThreadInterruptibleIO thread
	 */
	public static final void removeTargetFromCurrentThread(final Object target) {

		if (target == null) {
			log.error("null target; ignoring");
			return;
		}

		final Thread currentThread = Thread.currentThread();

		if (currentThread instanceof ThreadInterruptibleIO) {

			final ThreadInterruptibleIO interruptibleThread = //
			(ThreadInterruptibleIO) currentThread;

			interruptibleThread.removeTarget(target);

		} else {
			log.error("unsupported operation; ignoring; thread class : {}",
					currentThread.getClass().getName());
		}

	}

	private final void unregisterTarget(final Object target) {

		if (target == null) {
			log.error("null target; ignoring");
			return;
		}

		synchronized (interrupterList) {
			while (interrupterList.remove(target)) {
				// cleanup
			}
		}

	}

}
