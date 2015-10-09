package com.bitgo;
import java.io.File;

import java.io.File;
import org.bitcoinj.core.*;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.*;
import org.bitcoinj.utils.BriefLogFormatter;

public class txTest {
   static Address forwardingAddress;
   public static void main(String[] args){
       final WalletAppKit kit;
       //using log4j to log the running information
       BriefLogFormatter.init();
       if (args.length < 2) {
           System.err.println("INPUT VALUES MISSING!");
           return;
       }
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
       //translate the input address
       try {
           forwardingAddress = new Address(params, args[0]);
       } catch (AddressFormatException e) {
           e.printStackTrace();
       }
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
           public void onCoinsSent(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
               // Runs in the dedicated "user thread".
               Coin value = tx.getValueSentToMe(kit.wallet());
               System.out.println("Forwarding " + value.toFriendlyString() + " BTC");
// Now send the coins back! Send with a small fee attached to ensure rapid confirmation.
               final Coin amountToSend = value.subtract(Transaction.REFERENCE_DEFAULT_MIN_TX_FEE);
               try {
                   final Wallet.SendResult sendResult = kit.wallet().sendCoins(kit.peerGroup(), forwardingAddress, amountToSend);
               } catch (InsufficientMoneyException e) {
                   e.printStackTrace();
               }
           }
       });

       System.out.println("Sending");
   }
}