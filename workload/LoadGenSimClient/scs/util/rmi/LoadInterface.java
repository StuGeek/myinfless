// 
// Decompiled by Procyon v0.5.36
// 

package scs.util.rmi;

import java.rmi.RemoteException;
import java.rmi.Remote;

public interface LoadInterface extends Remote
{
    float getWindowAvgPerSecLatency(final int p0, final String p1) throws RemoteException;
    
    float getRealPerSecLatency(final int p0, final String p1) throws RemoteException;
    
    float getTotalQueryCount(final int p0) throws RemoteException;
    
    float getTotalRequestCount(final int p0) throws RemoteException;
    
    float getTotalAvgServiceRate(final int p0) throws RemoteException;
    
    int getRealQueryIntensity(final int p0) throws RemoteException;
    
    int getRealRequestIntensity(final int p0) throws RemoteException;
    
    void execStartHttpLoader(final int p0, final int p1, final int p2) throws RemoteException;
    
    void execStopHttpLoader(final int p0) throws RemoteException;
    
    int setIntensity(final int p0, final int p1) throws RemoteException;
}
/*
public interface LoadInterface extends Remote {
	public float getWindowAvgPerSecLatency(int serviceId,String metric) throws RemoteException; //return the value of Avg99th
	public float getRealPerSecLatency(int serviceId,String metric) throws RemoteException; //return the value of queryTime
	
	public float getTotalQueryCount(int serviceId) throws RemoteException; //return the value of queryTime (95th), unused
	public float getTotalRequestCount(int serviceId) throws RemoteException; //return the value of queryTime (99.9th), unused
	public float getTotalAvgServiceRate(int serviceId) throws RemoteException; //return the value of SR
	public int getRealQueryIntensity(int serviceId) throws RemoteException; //return the value of realQPS
	public int getRealRequestIntensity(int serviceId) throws RemoteException;  //return the value of realRPS
	
	public void execStartHttpLoader(int serviceId, int intensity, int concurrency) throws RemoteException; //start load generator for serviceId
	public void execStopHttpLoader(int serviceId) throws RemoteException; //stop load generator for serviceId
	public int setIntensity(int intensity,int serviceId) throws RemoteException; //change the RPS dynamically
}
 */