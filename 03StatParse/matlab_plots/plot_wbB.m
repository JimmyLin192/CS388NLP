function plot_wbB()

c4 = load('wbB/F1-vs-brown')

plot(c4(:,1),c4(:,2), 'g')
title('F1 Measure v.s. Self Train Set Size (Source: WSJ, Target: Brown)')
xlabel('Self Train Set Size')
ylabel('F1 Measure')
legend('F1-vs-wsj', 'Location', 'SouthEast')

