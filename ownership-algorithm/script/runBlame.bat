for /r %%v in (*.py) do node gitBlame-version1.js  "%%v" >> FixData/Stable/Owner10.txt
