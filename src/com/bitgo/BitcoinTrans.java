package com.bitgo;
import java.io.File;

import org.bitcoinj.core.*;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.*;
import org.bitcoinj.utils.BriefLogFormatter;
//import org.python.modules.thread.thread;

public class BitcoinTrans{
	@SuppressWarnings("deprecation")
	public static void main(String[] args){
		WalletAppKit kit;
		//log info	
		BriefLogFormatter.init();
		if (args.length < 2) {
			System.err.println("Usage: address-to-send-back-to [regtest|testnet]");
			return;
		}
	
		// Figure out which network we should connect to. Each one gets its own set of files.
		NetworkParameters params;
		String filePrefix;
		if (args[1].equals("testnet")) {
			params = TestNet3Params.get();
			filePrefix = "forwarding-service-testnet";
		} else if (args[1].equals("regtest")) {
			params = RegTestParams.get();
			filePrefix = "forwarding-service-regtest";
		} else {
			params = MainNetParams.get();
			filePrefix = "forwarding-service";
		}
	
	
		// Parse the address given as the first parameter.
		try {
			Address forwardingAddress = new Address(params, args[0]);
		} catch (AddressFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
		// Start up a basic app using a class that automates some boilerplate. Ensure we always have at least one key.
		kit = new WalletAppKit(params, new File("."), filePrefix) {
			@Override
			protected void onSetupCompleted() {
				// This is called in a background thread after startAndWait is called, as setting up various objects
				// can do disk and network IO that may cause UI jank/stuttering in wallet apps if it were to be done
				// on the main thread.
				if (wallet().getKeychainSize() < 1)
	            wallet().importKey(new ECKey());
			}
		};

		if (params == RegTestParams.get()) {
			// Regression test mode is designed for testing and development only, so there's no public network for it.
			// If you pick this mode, you're expected to be running a local "bitcoind -regtest" instance.
			kit.connectToLocalHost();
		}

		// Download the block chain and wait until it's done.
		kit.startAndWait();
	
		kit.wallet().addEventListener(new AbstractWalletEventListener() {
		    @Override
		    public void onCoinsReceived(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
		    	//thread.exit(); 
		        // Runs in the dedicated "user thread".
		    }
		});
		
		
	}
}
	