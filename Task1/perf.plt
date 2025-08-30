set datafile separator ","
set datafile columnheaders
set terminal pngcairo size 1400,900
set output "perf_plot.png"

set grid
set xlabel "X"
set ylabel "value (ns / ns^2)"
set title "Performance results vs X"
set key outside

plot \
    "performance_stats.csv" using 1:5:(stringcolumn(2) eq "10" ? $5 : 1/0) with linespoints lw 2 title "meanX (Y=10)", \
    "" using 1:7:(stringcolumn(2) eq "10" ? $7 : 1/0) with linespoints lw 2 title "meanY (Y=10)", \
    "" using 1:6:(stringcolumn(2) eq "10" ? $6 : 1/0) with linespoints lw 2 dt 2 title "varianceX (Y=10)", \
    "" using 1:8:(stringcolumn(2) eq "10" ? $8 : 1/0) with linespoints lw 2 dt 2 title "varianceY (Y=10)", \
    "" using 1:5:(stringcolumn(2) eq "20" ? $5 : 1/0) with linespoints lw 2 title "meanX (Y=20)", \
    "" using 1:7:(stringcolumn(2) eq "20" ? $7 : 1/0) with linespoints lw 2 title "meanY (Y=20)", \
    "" using 1:6:(stringcolumn(2) eq "20" ? $6 : 1/0) with linespoints lw 2 dt 2 title "varianceX (Y=20)", \
    "" using 1:8:(stringcolumn(2) eq "20" ? $8 : 1/0) with linespoints lw 2 dt 2 title "varianceY (Y=20)", \
    "" using 1:5:(stringcolumn(2) eq "30" ? $5 : 1/0) with linespoints lw 2 title "meanX (Y=30)", \
    "" using 1:7:(stringcolumn(2) eq "30" ? $7 : 1/0) with linespoints lw 2 title "meanY (Y=30)", \
    "" using 1:6:(stringcolumn(2) eq "30" ? $6 : 1/0) with linespoints lw 2 dt 2 title "varianceX (Y=30)", \
    "" using 1:8:(stringcolumn(2) eq "30" ? $8 : 1/0) with linespoints lw 2 dt 2 title "varianceY (Y=30)"

unset output
