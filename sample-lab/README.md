Here's how "one" specific commit is evaluated.

1. Jenkins job determines which commit to grade (`repo` and `sha`) based on timestamp.
2. It first fetches a student's repo (`git clone`) and then check out a specific commit (`git checkout` based on sha).
3. It copies student's `java/dataflow` directory into the judge's (my) local repo that contains shareable/hidden unit tests.
4. It runs `builder_base.sh` in the judge's repo (this has the same directory structure as student's repo), which builds docker container (but this is cached for each lab/project, so it will only be built once).
5. It then executes `runner-gcp.sh` which builds a docker container that contains the student's submission and the judge's unit tests. It then executes `gradle test` (only for `java/judge` directory as `java/dataflow` unit tests are ignored by the grading system). 
6. Upon a successful run, `gradle` will generate reports (html files) under `java/judge/build/reports/...` and those are copied over (from the docker container) to my VM's local directory. 
7. `judge-scripts/extract_reports-gcp.sh` will then fetch pass/fail results from the html files, and produces report files.

What needs to be done:
 - GRADING01 (Tushar & Hrishikesh): Check the said script above, and come up with a new script that is able to fetch error messages (during the `gradle test` step). Make a pull request for a new script when you're ready. You'll need to install Docker and locally test things (I have used these scripts on Mac and Linux, but not on Windows; figure things out!).
 - INFRA01 (Jie): Check if docker containers can be further optimized. The base image is created only once and cached thereafter, so I don't think that part needs further optimization.  
