#!/bin/bash
set -xe

# TOOD: Adjust per lab
is_test="yes"
weight_hidden=6000
weight_shareable=4000
exp_basic=10
exp_shareable=6
exp_hidden=7
#------------------------------

repo=$(cat ../info.csv | head -1 | cut -d',' -f1)
sha=$(cat ../info.csv | head -1 | cut -d',' -f2)

echo "[info] repo ${repo} sha ${sha}"

report_file=report.txt
hint_file=hint.txt
score_file=score.txt

rm -f $report_file
rm -f $hint_file
rm -f $score_file

touch $report_file
touch $hint_file
touch $score_file

echo "[init] created blank files"

# Obtain reports (html files) and parse.
set +e
files=(`ls "../output/classes/"`)
res_files=$?
set -e

if [[ $res_files -ne "0" ]]; then # error. no files. build/compilation error.
	echo "build/compilation errors or other errors while grading." > $report_file
	exit 123;
else
	for ((i=0; i<${#files[@]}; i++)); do
		fname=${files[$i]}
		class=$(echo "$fname" | sed -e 's/.html//g')

		if [[ $class != *".__Test"* ]]; then
			continue
		fi

		cat ../output/classes/$fname | grep -B2 '<td class="success">passed</td>' | awk 'NR%4 == 1' | sed -e 's/<.*">//g' | sed -e 's/<\/.*>//g' | sed -e 's/^/passed: '$class'./g' >> $report_file
		cat ../output/classes/$fname | grep -B2 '<td class="failures">failed</td>' | awk 'NR%4 == 1' | sed -e 's/<.*">//g' | sed -e 's/<\/.*>//g' | sed -e 's/^/failed: '$class'./g' >> $report_file
	done

	echo "[report] Created $report_file"
	no_line=$(wc -l "$report_file") || no_line="0"
	if [[ $no_line == "0" ]]; then
		echo "report file is blank.. aborting"
		exit 123;
	fi
fi

# Obtain hints if needed.
while read result; do
	if [[ $result != "failed:"* ]]; then
		continue
	fi
	if [[ $result != *"__shareable"* ]]; then
		continue
	fi
	echo "--- obtaining first hint---"
	fname=$(echo "$result" | cut -d' ' -f2 | sed -e 's/\./\//g' | sed 's/\(.*\)\//\1.java /' | cut -d' ' -f1)
	method=$(echo "$result" | cut -d' ' -f2 | sed -e 's/\./\//g' | sed 's/\(.*\)\//\1.java /' | cut -d' ' -f2)
	fname="../java/judge/src/test/java/$fname"
	echo "Hint for $result" >> $hint_file
	echo "" >> $hint_file
	sed -n '/'$method'/,/@Test/{x;p;d;}; x;' $fname | tac | sed -n '/}/,$p' | tac >> $hint_file
	break
done < $report_file

echo "[hint] Created $hint_file"

# Calculate score.
fname=$report_file
line=$(head -n 1 $fname)

if [[ $line == "[error]"* ]]; then
	exit 123;
else
  tot_basic=$(grep -v '^.*:.*\.__hidden' "$fname" | grep -v '^.*:.*\.__shareable' | wc -l | sed -e 's/^[[:space:]]*//g')
  tot_hidden=$(grep -r '^.*:.*__hidden' "$fname" | wc -l | sed -e 's/^[[:space:]]*//g')
  tot_shareable=$(grep -r '^.*:.*__shareable' "$fname" | wc -l | sed -e 's/^[[:space:]]*//g')


# TODO: To check the number of test cases here.
	if [[ $tot_basic -eq "${exp_basic}" && $tot_shareable -eq "${exp_shareable}" && $tot_hidden -eq "${exp_hidden}" ]]; then
		echo "OK! all good here."
	else # number of test cases does not match.
		echo "Tests were not executed properly due to infinite loop (time out) or compilation error or runtime error. You should check your code again before re-submitting." >> $report_file
		exit 123;
	fi

	cnt_basic=$(grep -v '^passed:.*\.__hidden' "$fname" | grep -v '^passed:.*\.__shareable' | grep -v '^failed:' | wc -l | sed -e 's/^[[:space:]]*//g')

	if [[ $cnt_basic -lt $tot_basic || $tot_hidden -eq "0" || $tot_shareable -eq "0" ]]; then
		score="0000"
		cnt_basic="0"
		cnt_hidden="0"
		cnt_shareable="0"
	else
		cnt_hidden=$(grep -r '^passed:.*__hidden' "$fname" | wc -l | sed -e 's/^[[:space:]]*//g')
		cnt_shareable=$(grep -r '^passed:.*__shareable' "$fname" | wc -l | sed -e 's/^[[:space:]]*//g')
		score=$(( (weight_hidden*cnt_hidden/tot_hidden) + (weight_shareable*cnt_shareable/tot_shareable) ))
	fi
fi

echo "$score,$cnt_basic/$tot_basic,$cnt_shareable/$tot_shareable,$cnt_hidden/$tot_hidden" > $score_file

echo "[score] Created $score_file"

echo "[update] Updating DB"

txt_report=$(cat $report_file)
txt_hint=$(cat $hint_file)
txt_score=$(cat $score_file)
# escape quotes.
txt_report="${txt_report//\'/\\\'}"
txt_hint="${txt_hint//\'/\\\'}"
txt_score="${txt_score//\'/\\\'}"

if [[ $is_test == "yes" ]]; then
	echo "Not writing to BQ."
else
	echo "Writing the grading results to BQ."
fi

exit 0
