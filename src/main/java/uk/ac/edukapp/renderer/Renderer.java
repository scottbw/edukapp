/*
 *  (c) 2012 University of Bolton
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

package uk.ac.edukapp.renderer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

import uk.ac.edukapp.model.Widgetprofile;

public class Renderer {
	
	public static String getDownloadUrl(Widgetprofile widgetprofile){

		// deduce whether is w3c or open social
		byte w3cOrOs = widgetprofile.getW3cOrOs();

		if (w3cOrOs == 0) {// is w3c
			return WidgetRenderer.getInstance()
					.getDownloadUrl(widgetprofile.getWidId());
		} else if (w3cOrOs == 1) {// is os
			return widgetprofile.getWidId();
		}
		return null;
		
	}


	public static String render(Widgetprofile widgetprofile, boolean wrap) {

		// deduce whether is w3c or open social
		byte w3cOrOs = widgetprofile.getW3cOrOs();

		if (w3cOrOs == 0) {// is w3c
			return WidgetRenderer.getInstance()
					.render(widgetprofile.getWidId(), wrap);
		} else if (w3cOrOs == 1) {// is os
			return GadgetRenderer.getInstance()
					.render(widgetprofile.getWidId());
		}
		return null;
	}

	public static String renderById(ServletContext ctx, String id) {
		EntityManagerFactory emf = (EntityManagerFactory) ctx
				.getAttribute("emf");
		EntityManager entityManager = emf.createEntityManager();
		Widgetprofile widgetprofile = entityManager.find(Widgetprofile.class,
				id);

		byte w3cOrOs = widgetprofile.getW3cOrOs();

		if (w3cOrOs == 0) {// is w3c
			return WidgetRenderer.getInstance()
					.render(widgetprofile.getWidId(), true);
		} else if (w3cOrOs == 1) {// is os
			return GadgetRenderer.getInstance()
					.render(widgetprofile.getWidId());
		}

		return null;
	}

	public static String renderById(ServletContext ctx, String id, int width,
			int height) {
		EntityManagerFactory emf = (EntityManagerFactory) ctx
				.getAttribute("emf");
		EntityManager entityManager = emf.createEntityManager();
		Widgetprofile widgetprofile = entityManager.find(Widgetprofile.class,
				id);

		byte w3cOrOs = widgetprofile.getW3cOrOs();

		if (w3cOrOs == 0) {// is w3c
			return WidgetRenderer.getInstance().render(
					widgetprofile.getWidId(), width, height, true);
		} else if (w3cOrOs == 1) {// is os
			return GadgetRenderer.getInstance().render(
					widgetprofile.getWidId(), width, height);
		}

		return null;
	}
}
