echo "FILE_NAME;FILE_LINES;AUTHOR_NAME;AUTHOR_LINES" > $1

for filename in `find . -type f -not -path "./.git/*" -not -path "./.idea/*"`
do
    file_lines=$(wc -l < $filename)
    git blame --line-porcelain $filename | grep "^author " | sed -e "s/^author //" | sort | uniq -c | sort -nr | awk '{ lines = $1; $1 = ""; print $0 ";" lines; }' | sed -e "s/^ //"  -e "s|^|$filename;$file_lines;|" >> $1

done
