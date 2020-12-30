package com.fourteenosix;

import com.fourteenosix.Factories.RPSFactory;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	    String token = System.getenv("BOT_TOKEN");
	    String channelToRespondIn = System.getenv("BOT_CHAN");
		Bot b = new Bot(token, channelToRespondIn);



		if (b.isReady()){
			b.start();
			b.addGame(new RPSFactory());

			b.waitForDisconnect();
		} else{
			System.out.println("Could not start Bot");
		}


		Scanner input = new Scanner(System.in);





    }

}
