function plot_bwA()

c1 = load('bwA/c1-out-domain-self-train')
c2 = load('bwA/c2-in-domain')
c3 = load('bwA/c3-out-domain')

plot(c1(:,1),c1(:,2), 'r', c2(:,1),c2(:,2), 'g', c3(:,1),c3(:,2), 'b')
title('Three Comparative Learning Curves (Source: Brown, Target: WSJ)')
xlabel('Train Seed Set Size')
ylabel('F1 Measure')
legend('c1-out-domain-adapted','c2-in-domain','c3-out-domain', 'Location', 'SouthEast')

