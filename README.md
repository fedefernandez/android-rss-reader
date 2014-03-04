# Android RSS reader library

Easy to Use
======

    SimpleFeedReader feedReader = new SimpleFeedReader();
    Feed feed = feedReader.getFeed("http://android-developers.blogspot.com/atom.xml");

Features
======

* Designed to be used on android
* No dependencies
* Easy to use

Including In Your Project
======

This library is presented as a `.jar` file which you can include in the libs/ folder of your application. 
You can download the latest version from the [downloads page][1].

If you are a Maven user you can include the library by specifying it as a dependency (**IMPORTANT: the library is not available on maven central repo**):

    <dependency>
        <groupId>com.projectsexception</groupId>
        <artifactId>android-rss-reader</artifactId>
        <version>0.0.1</version>
    </dependency>

Developed By
============

* Fede Fernández - <fedeproex@gmail.com>

License
======

Copyright 2013 [Projects Exception][2]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0][3]

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[1]: https://bitbucket.org/fedeproex/android-rss-reader/downloads
[2]: http://projectsexception.com
[3]: http://www.apache.org/licenses/LICENSE-2.0
