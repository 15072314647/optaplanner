************************************************************************
file with basedata            : mf45_.bas
initial value random generator: 705555335
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  32
horizon                       :  240
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     30      0       23        6       23
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          3           6  10  17
   3        3          3           5  12  28
   4        3          3           9  20  28
   5        3          3           7   8  19
   6        3          3          11  12  27
   7        3          1          21
   8        3          3          13  14  25
   9        3          2          13  19
  10        3          3          15  23  24
  11        3          2          16  24
  12        3          3          13  18  22
  13        3          2          21  26
  14        3          1          27
  15        3          2          25  26
  16        3          3          19  20  22
  17        3          1          26
  18        3          3          21  25  30
  19        3          1          29
  20        3          1          31
  21        3          1          24
  22        3          1          30
  23        3          2          27  28
  24        3          1          29
  25        3          1          31
  26        3          1          30
  27        3          1          31
  28        3          1          29
  29        3          1          32
  30        3          1          32
  31        3          1          32
  32        1          0        
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration  R 1  R 2  N 1  N 2
------------------------------------------------------------------------
  1      1     0       0    0    0    0
  2      1     2       9    4    5    8
         2     4       7    4    5    8
         3     8       5    3    5    5
  3      1     1       7    3    6    4
         2     2       7    2    4    3
         3     4       6    2    3    3
  4      1     4       5    5    7    8
         2     4       6    5    6    8
         3     6       4    2    3    5
  5      1     2       9   10    7    3
         2     3       9    8    6    3
         3     6       9    7    3    2
  6      1     2       5   10    5    5
         2     8       4    7    4    5
         3     9       4    5    4    5
  7      1     6       7    7    5   10
         2     6       8    7    4   10
         3     8       6    6    4    3
  8      1     3       6   10    8    4
         2     9       3    8    5    3
         3     9       2    7    7    4
  9      1     4       4    8    8    7
         2     7       4    8    5    4
         3     9       2    5    3    4
 10      1     3       5    7    3    2
         2     4       5    4    3    2
         3     7       5    4    2    2
 11      1     3       7    4    8    5
         2     5       7    2    5    4
         3     6       2    2    4    4
 12      1     5       8    8    2    8
         2     6       6    4    2    8
         3    10       4    1    1    7
 13      1     3       7    5   10   10
         2     5       4    5    6   10
         3     9       4    4    2    9
 14      1     2       3    7    8    5
         2     6       2    7    7    3
         3    10       2    7    7    1
 15      1     1       3    8   10    8
         2     5       3    6    8    6
         3     7       2    5    5    3
 16      1     5       8    7    7    5
         2     7       5    7    5    3
         3     9       5    5    4    2
 17      1     1       5   10    7    8
         2     5       4    7    7    6
         3     9       3    6    3    5
 18      1     5       7    6    6    7
         2     5       7    4    6   10
         3    10       6    4    6    3
 19      1     6       9    7    6    7
         2     7       9    4    4    7
         3    10       8    4    4    6
 20      1     1      10    3    7    8
         2     6      10    2    5    8
         3     6      10    3    5    7
 21      1     2       6   10    6    9
         2     7       6   10    5    8
         3    10       2    9    4    8
 22      1     6       5    6    9    4
         2     7       5    6    8    4
         3     9       2    5    7    4
 23      1     2       6    4   10    7
         2    10       5    4    8    4
         3    10       4    4    7    5
 24      1     1       7    3    8    8
         2     2       7    1    3    7
         3     2       7    1    4    6
 25      1     2       8    7    9    8
         2     5       7    5    7    7
         3     7       6    3    7    2
 26      1     6       8    3    9    4
         2     9       7    3    8    3
         3    10       6    2    7    2
 27      1     4       3    9    6    5
         2     8       2    6    5    4
         3     8       3    4    4    4
 28      1     1      10    7   10    7
         2     9       7    5    9    6
         3    10       4    3    8    3
 29      1     4       9    9    5    6
         2     9       8    3    3    3
         3     9       7    4    3    2
 30      1     5       8    7    2    7
         2     5       8    6    3    7
         3     6       7    5    2    6
 31      1     2       5    7    8   10
         2     4       5    7    7    6
         3     7       4    5    7    6
 32      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   18   19  170  163
************************************************************************