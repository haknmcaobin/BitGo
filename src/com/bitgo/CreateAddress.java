package com.bitgo;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;



public class CreateAddress {

    public static void main(String[] args) throws Exception {

        // use test net by default
        String net = "test";
        
        if (args.length >= 1 && (args[0].equals("test") || args[0].equals("prod"))) {
            net = args[0];
            System.out.println("Using " + net + " network.");
        }
        
        // create a new EC Key ...
        ECKey key = new ECKey();

        // ... and look at the key pair
        System.out.println("We created key:\n" + key);
        
        // either test or production net are possible
        final NetworkParameters netParams;
        
        if (net.equals("prod")) {
            netParams = MainNetParams.get();
            } else {
            netParams = TestNet3Params.get();
        }
        
        // get valid Bitcoin address from public key
        Address addressFromKey = key.toAddress(netParams);
        
        System.out.println("On the " + net + " network, we can use this address:\n" + addressFromKey);
    }
}