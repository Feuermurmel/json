# feuermurmel-json

## How to release

```sh
$ mvn release:prepare
[INFO] Scanning for projects...
[...]
What is the release version for "feuermurmel-json"? (ch.feuermurmel.json:feuermurmel-json) 0: : 0.3.0
What is SCM release tag or label for "feuermurmel-json"? (ch.feuermurmel.json:feuermurmel-json) 0.3.0: : 
What is the new development version for "feuermurmel-json"? (ch.feuermurmel.json:feuermurmel-json) 0.3.1-SNAPSHOT: : 
[INFO] Transforming 'feuermurmel-json'...
[...]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 26.211 s
[INFO] Finished at: 2018-03-16T15:11:35+01:00
[INFO] ------------------------------------------------------------------------
```

```
$ git checkout 0.3.0
HEAD is now at 6ebcc6f... [maven-release-plugin] prepare release 0.3.0
```

```
$ rm -rf mvn-repo/
```

```
$ git clone -b mvn-repo . mvn-repo
Cloning into 'mvn-repo'...
done.
```

```
$ mvn deploy -DaltDeploymentRepository=repo::default::file://mvn-repo
[INFO] Scanning for projects...
[...]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 6.355 s
[INFO] Finished at: 2018-03-16T15:12:49+01:00
[INFO] ------------------------------------------------------------------------
```

```
$ git checkout master
```

```
$ cd mvn-repo/
```

```
$ git add -A
add 'ch/feuermurmel/json/feuermurmel-json/maven-metadata.xml'
add 'ch/feuermurmel/json/feuermurmel-json/maven-metadata.xml.md5'
add 'ch/feuermurmel/json/feuermurmel-json/maven-metadata.xml.sha1'
add 'ch/feuermurmel/json/feuermurmel-json/0.3.0/feuermurmel-json-0.3.0-javadoc.jar'
add 'ch/feuermurmel/json/feuermurmel-json/0.3.0/feuermurmel-json-0.3.0-javadoc.jar.md5'
add 'ch/feuermurmel/json/feuermurmel-json/0.3.0/feuermurmel-json-0.3.0-javadoc.jar.sha1'
add 'ch/feuermurmel/json/feuermurmel-json/0.3.0/feuermurmel-json-0.3.0-sources.jar'
add 'ch/feuermurmel/json/feuermurmel-json/0.3.0/feuermurmel-json-0.3.0-sources.jar.md5'
add 'ch/feuermurmel/json/feuermurmel-json/0.3.0/feuermurmel-json-0.3.0-sources.jar.sha1'
add 'ch/feuermurmel/json/feuermurmel-json/0.3.0/feuermurmel-json-0.3.0.jar'
add 'ch/feuermurmel/json/feuermurmel-json/0.3.0/feuermurmel-json-0.3.0.jar.md5'
add 'ch/feuermurmel/json/feuermurmel-json/0.3.0/feuermurmel-json-0.3.0.jar.sha1'
add 'ch/feuermurmel/json/feuermurmel-json/0.3.0/feuermurmel-json-0.3.0.pom'
add 'ch/feuermurmel/json/feuermurmel-json/0.3.0/feuermurmel-json-0.3.0.pom.md5'
add 'ch/feuermurmel/json/feuermurmel-json/0.3.0/feuermurmel-json-0.3.0.pom.sha1'
```

```
$ git commit -m "Deployment of version 0.3.0"
[mvn-repo 8ef5e57] Deployment of version 0.3.0
 15 files changed, 92 insertions(+), 4 deletions(-)
[...]
```

```
$ git push
Counting objects: 22, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (8/8), done.
Writing objects: 100% (22/22), 455.85 KiB | 15.19 MiB/s, done.
Total 22 (delta 2), reused 0 (delta 0)
To [...]/json/.
   69dbcf2..8ef5e57  mvn-repo -> mvn-repo
```

```
$ cd ..
```

```
$ git push origin master mvn-repo 0.3.0
Counting objects: 410, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (88/88), done.
Writing objects: 100% (410/410), 2.50 MiB | 627.00 KiB/s, done.
Total 410 (delta 84), reused 329 (delta 68)
remote: Resolving deltas: 100% (84/84), done.
To github.com:Feuermurmel/json.git
 * [new branch]      mvn-repo -> mvn-repo
 * [new tag]         0.3.0 -> 0.3.0
 + 0c98381...a15abee master -> master
```
