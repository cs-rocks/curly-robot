#!/bin/bash
BUCKET="usf-cs686-sp20"
HW="cs686-lab02"

set -xe

# info.csv contains repo,sha,email,team,poke (poke is always 2-digit).
info=$(cat info.csv | head -1)
repo=$(echo ${info} | cut -d',' -f1)
sha=$(echo ${info} | cut -d',' -f2)
email=$(echo ${info} | cut -d',' -f3)
team=$(echo ${info} | cut -d',' -f4)
poke=$(echo ${info} | cut -d',' -f5)

if [[ ${#sha} -eq 40 && $repo == "${HW}-"* ]]; then
	echo "Proceeding with ${repo} at ${sha}"
else
	echo "${sha} is not of 40 chars long or ${repo} has improper prefix."
	exit 1
fi

LOCAL_DIR=report-${team}-${poke}-${sha:0:7}

# remove this file just in case.
gsutil rm gs://${BUCKET}/${HW}/${LOCAL_DIR}.zip || true

# begin grading.
# build runner container.
rnd_suffix=$RANDOM
docker_tag="${sha}.${rnd_suffix}"
docker_id="${sha}-${rnd_suffix}"

echo "using docker id: ${docker_id} and tag: ${docker_tag}"

rm -rf java/dataflow/build
rm -rf java/judge/build
docker build -t runner:${docker_tag} -f dockers/DockerRunner .

rm -rf output
mkdir -p output
docker rm ${docker_id} || true

# docker run step may fail, if gradle test fails. that's OK.
docker run -u gradle --name ${docker_id} runner:${docker_tag} || true

# now, copy step shouldn't fail. if it fails, that means not all tests were run.
set +e
docker cp "${docker_id}:/home/java/judge/build/reports/tests/test/classes" output
cp_res=$?
set -e
if [[ $cp_res -ne "0" ]]; then # error while copying. It means tests were not run successfully.
	echo "returned code for docker cp: ${cp_res}"
	exit 101
fi

# Obtaining score/report files
pushd judge-scripts
rm -f *.txt
set +e
bash extract_reports-gcp.sh
report_res=$?
set -e
popd

if [[ $report_res -eq "0" ]]; then
	echo "score was updated successfully."
else
	echo "[error] updating commit/score with error"
	exit 102
fi

# Upload reports to GCS.
# Set permission based on the user's email.
mkdir -p ${LOCAL_DIR} 
cp judge-scripts/*.txt ${LOCAL_DIR}/
zip -r ${LOCAL_DIR}.zip ${LOCAL_DIR}
gsutil cp ${LOCAL_DIR}.zip gs://${BUCKET}/${HW}/${LOCAL_DIR}.zip
gsutil acl ch -u ${email}:R gs://${BUCKET}/${HW}/${LOCAL_DIR}.zip
rm -rf ${LOCAL_DIR}
rm -f ${LOCAL_DIR}.zip

