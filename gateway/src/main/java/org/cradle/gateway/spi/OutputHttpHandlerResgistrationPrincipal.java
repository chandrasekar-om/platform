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
package org.cradle.gateway.spi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.cradle.gateway.BasicHttpHandler;
import org.cradle.gateway.HttpAdapter;
import org.cradle.gateway.HttpMethod;
import org.cradle.gateway.restful.ResponseObject;
import org.cradle.gateway.restful.exception.RESTfulException;
import org.cradle.gateway.restful.exception.RedirectException;

/**
 * @author	Sherief Shawky
 * @email 	mcrakens@gmail.com
 * @date 	Apr 15, 2015
 */
public class OutputHttpHandlerResgistrationPrincipal extends HttpHandlerResgisterationPrinicipal {

	/**
	 * @param next
	 */
	public OutputHttpHandlerResgistrationPrincipal(RegistrationPrincipal next) {
		super(next);
	}

	/**
	 * @param method
	 * @return
	 */
	protected boolean isMethodSupported(HttpMethod.Method method) {

		return method.equals(HttpMethod.Method.GET) || method.equals(HttpMethod.Method.DELETE);
	}

	/**
	 * @param handler
	 * @param target
	 * @param annotation
	 * @return
	 */
	protected BasicHttpHandler createHttpHandler(final Object handler,
			final Method target, final HttpMethod annotation) {

		return new OutputHttpHandler() {

			@Override
			protected ResponseObject execute(HttpAdapter httpAdapter)
					throws RedirectException, RESTfulException {

				try {

					return new ResponseObject(target.invoke(handler, httpAdapter), annotation.contentType());

				} catch (IllegalAccessException | IllegalArgumentException e) {

					throw new RuntimeException(e);

				} catch (InvocationTargetException e) {

					Throwable targetException  = e.getTargetException();
					
					if(targetException instanceof RedirectException)
						throw (RedirectException) targetException;
				
					if(targetException instanceof RESTfulException)
						throw (RESTfulException) targetException;

					throw new RuntimeException(e);
				}

			}

		};
	}

	/* (non-Javadoc)
	 * @see org.cradle.gateway.spi.HttpHandlerResgisterationPrinicipal#isAnnotationValid(java.lang.reflect.Method, org.cradle.gateway.HttpMethod)
	 */
	@Override
	protected void isAnnotationValid(Method target, HttpMethod annotation) {
		
		if(target.getParameterCount() != 1){

			throw new RuntimeException("One parameter is allowed for GET & DELETE methods");
		}

		checkHttpAdapterParam(target);
		
	}
}