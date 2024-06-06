package main;

import processing.core.PApplet;

public class DiffusionManager {
    int N = 600; // points number of UV

    // System parameters
    double diffU;
    double diffV;
    double paramF; // feed rate
    double paramK; // kill rate

    boolean rndInitCondition;

    double[][] U = new double[N][N];
    double[][] V = new double[N][N];

    double[][] dU = new double[N][N];
    double[][] dV = new double[N][N];

    int[][] offset = new int[N][2];

    FileLoader fl;

    public DiffusionManager(PApplet app) {
        diffU = 0.07;
        diffV = 0.03;
        paramF = 0.052;
        paramK = 0.064;

        rndInitCondition = true;

        // Populate U and V with initial data
        generateInitialState(app);

        // Set up offsets
        for (int i = 1; i < N - 1; i++) {
            offset[i][0] = i - 1;
            offset[i][1] = i + 1;
        }

        offset[0][0] = N - 1;
        offset[0][1] = 1;

        offset[N - 1][0] = N - 2;
        offset[N - 1][1] = 0;

        fl = new FileLoader(app);
//		System.out.println("*********"+fl.site.pixels[0*600+300]);
    }

    public void generateInitialState(PApplet app) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                U[i][j] = 0.7;
                V[i][j] = 0.0;
            }
        }

        if (rndInitCondition) {
            for (int i = 470; i < 490; i++) {
                for (int j = 400; j < 420; j++) {
                    U[i][j] = 0.5 * (1 + app.random(-1, 1));
                    V[i][j] = 0.25 * (1 + app.random(-1, 1));
                }
            }
//			for (int i = 180; i < 220; i++) {
//				for (int j = 420; j < 450; j++) {
//					U[i][j] = 0.5 * (1 + app.random(-1, 1));
//					V[i][j] = 0.25 * (1 + app.random(-1, 1));
//				}
//			}
//			for (int i = 480; i < 520; i++) {
//				for (int j = 360; j < 400; j++) {
//					U[i][j] = 0.5 * (1 + app.random(-1, 1));
//					V[i][j] = 0.25 * (1 + app.random(-1, 1));
//				}
//			}
        } else {
            for (int i = 470; i < 490; i++) {
                for (int j = 400; j < 420; j++) {
                    U[i][j] = 0.5;
                    V[i][j] = 0.25;
                }
            }
//			for (int i = 180; i < 220; i++) {
//				for (int j = 380; j < 410; j++) {
//					U[i][j] = 0.5;
//					V[i][j] = 0.25;
//				}
//			}
//			for (int i = 480; i < 520; i++) {
//				for (int j = 330; j < 370; j++) {
//					U[i][j] = 0.5;
//					V[i][j] = 0.25;
//				}
//			}
        }
    }

    public void timestep() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                double u = U[i][j];
                double v = V[i][j];

                int left = offset[i][0];
                int right = offset[i][1];
                int up = offset[j][0];
                int down = offset[j][1];

                double uvv = u * v * v;

                double lapU = (U[left][j] + U[right][j] + U[i][up] + U[i][down] - 4 * u);
                double lapV = (V[left][j] + V[right][j] + V[i][up] + V[i][down] - 4 * v);

                dU[i][j] = diffU * lapU - uvv + paramF * (1 - u);
                dV[i][j] = diffV * lapV + uvv - (paramK + paramF) * v;
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < 1; k++) {
//					if (checkCircle(200, 200, i, j) == false) {
//						U[i][j] += dU[i][j];
//						V[i][j] += dV[i][j];
//					} else {
//						U[i][j] = U[i][j];
//						V[i][j] = V[i][j];
//					}
                    if (fl.pixGrey.get(j * 600 + i) > 0.5) {
                        U[i][j] += dU[i][j];
                        V[i][j] += dV[i][j];
                    } else {
                        U[i][j] = U[i][j];
                        V[i][j] = V[i][j];
                    }
                }
            }
        }
    }

    public void display(PApplet app) {
        // Draw points
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                app.set(i, j, app.color(0f, 0f, (float) (1 - U[i][j])));
            }
        }
    }

    public void displaySite(PApplet app) {
        //app.image(fl.site_yuhua, 0, 0, app.width, app.height);
    }

}
