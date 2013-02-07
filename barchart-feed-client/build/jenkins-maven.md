<!--

    Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>

    All rights reserved. Licensed under the OSI BSD License.

    http://www.opensource.org/licenses/bsd-license.php

-->
### Goals and options

clean deploy --update-snapshots --show-version --activate-profiles ${distribution}

### Release goals and options

w/o site:

release:prepare release:perform -Dresume=false  --define releaseProfiles=${distribution}

with site:

release:prepare release:perform -Dresume=false  --define releaseProfiles=${distribution} --define releasePrepareGoals="clean install" --define releasePerformGoals="deploy site"

###

