package org.codehaus.yagll.annotation
/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 7, 2009
 * Time: 12:22:03 AM
 * To change this template use File | Settings | File Templates.
 */

public class SelfAware {

  static boolean isDirectoryBound(test, object) {
    def annotations = object.class.getDeclaredAnnotations()
    def annotationTypes = annotations*.annotationType()
    def annotationNames = annotationTypes*.getName()
    annotationNames.contains("org.codehaus.yagll.annotation.DirectoryBound")
  }

  static List getEntities(test, object) {
    if (isDirectoryBound(test, object)) {
      def annotations = object.class.getDeclaredAnnotations()
      def annotation = annotations.find { it.annotationType().name == "org.codehaus.yagll.annotation.DirectoryBound" }
      return annotation.entities() as List
    }
    return []
  }
}