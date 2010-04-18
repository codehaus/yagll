package org.codehaus.yagll.annotation

import org.testng.annotations.Test

/**
 * Created by IntelliJ IDEA.
 * User: brett
 * Date: Mar 7, 2009
 * Time: 12:06:53 AM
 * To change this template use File | Settings | File Templates.
 */

public class DirectoryBoundTestNG {

  @Test
  void shouldSucceedBoundTypeKnowsItIsDirectoryBound() {
    def selfAwareDirectoryBoundInstance = new BoundType()
    use (SelfAware) {
      assert isDirectoryBound(selfAwareDirectoryBoundInstance)
    }
  }

  @Test
  void shouldSucceedUnboundTypeKnowsItIsNotDirectoryBound() {
    def selfAwareDirectoryUnboundInstance = new UnboundType()
    use (SelfAware) {
      assert !isDirectoryBound(selfAwareDirectoryUnboundInstance)
    }
  }

  @Test
  void shouldSucceedWithEmptyListIfNotBound() {
    def selfAwareDirectoryUnboundInstance = new UnboundType()
    use (SelfAware) {
      assert getEntities(selfAwareDirectoryUnboundInstance) == []
    }
  }

  @Test
  void shouldSucceedWithEmptyListIfBoundToNoEntities() {
    def selfAwareDirectoryBoundInstance = new BoundType()
    use (SelfAware) {
      assert getEntities(selfAwareDirectoryBoundInstance) == []
    }
  }

  @Test
  void shouldSucceedWithOneMemberListIfBoundToOneEntity() {
    def selfAwareDirectoryBoundToSingleAccountInstance = new BoundToSingleAccountType()
    use (SelfAware) {
      assert getEntities(selfAwareDirectoryBoundToSingleAccountInstance) == ["Test Account"]
    }
  }

  @Test
  void shouldSucceedWithListIfBoundToMultipleEntities() {
    def selfAwareDirectoryBoundToMultipleTestAccountsInstance = new BoundToMultipleTestAccountsType()
    use (SelfAware) {
      assert getEntities(selfAwareDirectoryBoundToMultipleTestAccountsInstance) == ["Test Account", "Test Account", "Real Account"]
    }
  }

}