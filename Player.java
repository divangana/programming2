package Programming2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import javafx.util.Pair;

enum PlayerTypes {
	T4T, G, AC, AD
}

public class Player {
	public static void main(String[] args) {

		// Create a Scanner object; this will take input
		Scanner input = new Scanner(System.in);  // Create a Scanner object
		System.out.println("What are n, m, p, and k?");

		// Read user input
		// Number of players
		int n = input.nextInt(); // Number of players
		int m = input.nextInt(); // How many games two players play with each other
		int p = input.nextInt(); // Percentage of players to eliminate
		int k = input.nextInt(); // Number of generations

		int[] payoff = new int[n];
		PlayerTypes[] style = new PlayerTypes[n];
		boolean[] nextMove = new boolean[n];
		
		// question 1 player types division!
		for(int i=0; i<n/4; i++) {
			style[i] = PlayerTypes.T4T;
			style[i+n/4] = PlayerTypes.G;
			style[i+n/2] = PlayerTypes.AC;
			style[i+(3*n)/4] = PlayerTypes.AD;

			nextMove[i] = true;
			nextMove[i+n/4] = true;
			nextMove[i+n/2] = true;
			nextMove[i+(3*n)/4] = false;
		}
		
		
		// question 2 player types division! was not creative so i went with the example in the hw assignment
//		style[0] = PlayerTypes.G;
//		style[1] = PlayerTypes.AC;
//		
//		for(int i = 2; i<n; i++) {
//			style[i] = PlayerTypes.T4T;
//		}
		
		
		for(int i = 1; i <=k; i++) {
			for(int p1 = 0; p1 < n; p1++) {
				for(int p2 = 0; p2 < n; p2++) {
					
					// How to calculate payoffs based on moves
					if(p1!=p2) {
						
						// Actually play m games
						for(int games = 1; games <= m; games++) {
							if(nextMove[p1]==true) {
								if(nextMove[p2]==true) {
									payoff[p1]+=3;
									payoff[p2]+=3;
								}
								else {
									payoff[p2]+=5;
								}
							} else {
								if(nextMove[p2]==true) {
									payoff[p1]+=5;
								}
								else {
									payoff[p1]+=1;
									payoff[p2]+=1;
								}
							}
							if(style[p1]==PlayerTypes.T4T) {
								nextMove[p1]=nextMove[p2];
							}
							if(style[p1]==PlayerTypes.G && nextMove[p2]==false) {
								nextMove[p1]=false;
							}
							if(style[p2]==PlayerTypes.T4T) {
								nextMove[p2]=nextMove[p1];
							}
							if(style[p2]==PlayerTypes.G && nextMove[p1]==false) {
								nextMove[p2]=false;
							}
						}
						
						//Resetting initial moves
						for(int j = 0; j < n; j++) {
							if(style[j]==PlayerTypes.T4T || style[j]==PlayerTypes.G) {
								nextMove[j]=true;
							}
						}
					}
				}
			}
			
			int[] copyPayoff = payoff;
			ArrayList<Pair<PlayerTypes, Integer>> sorted = new ArrayList(n);
			for(int x = 0; x < n; x++) {
				sorted.add(new Pair<PlayerTypes, Integer>(style[x],x));
			}
			Collections.sort(sorted, Comparator.comparing(s -> copyPayoff[s.getValue()]));
//			System.out.println(Arrays.toString(style));
//			System.out.println(Arrays.toString(payoff));
//			System.out.println(sorted.toString());
			
			//Count players by type
			//Sum payoff by type
			int[] count = new int[4];
			int[] sums = new int[4];
			for(int q = 0; q < n; q++) {
				int index = 0;
				if(style[q]==PlayerTypes.T4T) {
					index = 0;
				} 
				else if(style[q]==PlayerTypes.G) {
					index = 1;
				}
				else if(style[q]==PlayerTypes.AC) {
					index = 2;
				}
				else {
					index = 3;
				}
				count[index]++;
				sums[index]+= payoff[q];
			}
			
			// Average payoff per type
			// Total payoff
			int[] avg = new int[4];
			int total = 0;
			
			for(int f = 0; f < 4; f++) {
				if(sums[f]==0||count[f]==0) {
					avg[f]=0;
				}
				else {
					avg[f] = (sums[f]/count[f]);	
				}
				total+=sums[f];
			}
			
			
			System.out.println("Generation " + i +":");
			System.out.println("Types of players: [T4T, G, AC, AD]");
			System.out.println("Percentage of players by type: " + Arrays.toString(count));
			System.out.println("Payoff by player type: " + Arrays.toString(sums));
			System.out.println("Average payoff by player type: " + Arrays.toString(avg));
			System.out.println("Total sum of payoff this generation: " + total + "\n");
			
			// Remove first p and duplicate last p into the "empty" spaces
			for(int w = 0; w < n; w++) {
				if(w < p) {
					style[w] = sorted.get(n-w-1).getKey();
				} 
				else {
					style[w] = sorted.get(w).getKey();
				}
			}
			// reset payoff matrix for next generation
			payoff = new int[n];
		}
	}
}