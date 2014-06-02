/*
 *  (c) 2014 University of Bolton
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
 * limitations under the License.
 */

package uk.ac.edukapp.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Kris Popat
 *
 */

public class CorsFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		((HttpServletResponse)response).addHeader("Access-Control-Allow-Origin", "*");
		String method = ((HttpServletRequest)request).getMethod();
		if ( "OPTIONS".equalsIgnoreCase(method)) {
			((HttpServletResponse)response).addHeader("Access-Control-Allow-Methods", "OPTIONS");
			((HttpServletResponse)response).addHeader("Access-Control-Allow-Credentials", "true");
		}
		chain.doFilter(request, response);

	}

	public void init(FilterConfig arg0) throws ServletException {
		

	}

}
