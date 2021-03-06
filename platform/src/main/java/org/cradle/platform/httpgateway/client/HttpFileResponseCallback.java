/**
 * 
 */
package org.cradle.platform.httpgateway.client;

import java.io.File;

/**
 * @author 	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Nov 12, 2014
 */
public interface HttpFileResponseCallback {
	
	public void response(File result);
	
	public void error(Throwable e);
}
