function plot_bwB()

c4 = load('bwB/F1-vs-wsj')

plot(c4(:,1),c4(:,2), 'g')
title('F1 Measure v.s. Self Train Set Size (Source: Brown, Target: WSJ)')
xlabel('Self Train Set Size')
ylabel('F1 Measure')
legend('F1-vs-wsj', 'Location', 'SouthEast')

