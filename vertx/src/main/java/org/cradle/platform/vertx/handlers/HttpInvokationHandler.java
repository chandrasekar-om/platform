/**
 *  Copyright mcplissken.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.cradle.platform.vertx.handlers;

import org.cradle.platform.httpgateway.HttpAdapter;
import org.cradle.platform.httpgateway.exception.HttpException;
import org.cradle.platform.httpgateway.filter.FilterInvokationHandler;
import org.cradle.platform.vertx.httpgateway.VertxHttpAdapter;
import org.cradle.reporting.SystemReportingService;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

/**
 * @author Sherief Shawky
 * @email mcrakens@gmail.com
 * @date Aug 25, 2014
 */
public class HttpInvokationHandler implements Handler<HttpServerRequest> {

	private FilterInvokationHandler filterInvokationHandler;

	private SystemReportingService reportingService;

	public HttpInvokationHandler(FilterInvokationHandler startFilter, SystemReportingService reportingService) {

		filterInvokationHandler = startFilter;

		this.reportingService = reportingService;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vertx.java.core.Handler#handle(java.lang.Object)
	 */
	@Override
	public void handle(HttpServerRequest request) {

		HttpAdapter httpAdapter = new VertxHttpAdapter(request, request.response());

		try {

			filterInvokationHandler.filter(httpAdapter);

		} catch (HttpException e) {

			if(reportingService != null){
				reportingService.exception(this.getClass().getSimpleName(), SystemReportingService.FILE, e);
			}

			httpAdapter.exception(e);

		} catch (Exception e) {
			
			if(reportingService != null){
				reportingService.exception(this.getClass().getSimpleName(), SystemReportingService.MAIL, e);
			}

			httpAdapter.error(e);
		}
	}

}