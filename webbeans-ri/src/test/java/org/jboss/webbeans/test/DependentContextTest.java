package org.jboss.webbeans.test;

import static org.jboss.webbeans.util.BeanFactory.createProducerMethodBean;
import static org.jboss.webbeans.util.BeanFactory.createSimpleBean;

import java.lang.reflect.Method;

import javax.webbeans.ContextNotActiveException;
import javax.webbeans.Dependent;
import javax.webbeans.manager.Bean;

import org.jboss.webbeans.bean.ProducerMethodBean;
import org.jboss.webbeans.bean.SimpleBean;
import org.jboss.webbeans.contexts.DependentContext;
import org.jboss.webbeans.test.beans.Fox;
import org.jboss.webbeans.test.beans.FoxRun;
import org.jboss.webbeans.test.beans.SpiderProducer;
import org.jboss.webbeans.test.beans.Tarantula;
import org.testng.annotations.Test;

@SpecVersion("PDR")
public class DependentContextTest extends AbstractTest
{
   
   @Test(groups={"contexts", "injection"}) @SpecAssertion(section="8.3")
   public void testInstanceNotSharedBetweenInjectionPoints()
   {
      Bean<FoxRun> foxRunBean = createSimpleBean(FoxRun.class);
      Bean<Fox> foxBean = createSimpleBean(Fox.class);
      manager.addBean(foxBean);
      FoxRun foxRun = foxRunBean.create();
      assert !foxRun.fox.equals(foxRun.anotherFox);
   }
   
   @Test(groups={"stub", "contexts", "el"}) @SpecAssertion(section="8.3")
   public void testInstanceUsedForElEvalutionNotShared()
   {
      assert false;
   }
   
   @Test(groups={"contexts", "producerMethod"}) @SpecAssertion(section="8.3")
   public void testInstanceUsedForProducerMethodNotShared() throws Exception
   {
      SimpleBean<SpiderProducer> spiderProducer = createSimpleBean(SpiderProducer.class); 
      manager.addBean(spiderProducer);
      Method method = SpiderProducer.class.getMethod("produceTarantula");
      ProducerMethodBean<Tarantula> tarantulaBean = createProducerMethodBean(Tarantula.class, method, spiderProducer);
      Tarantula tarantula = tarantulaBean.create();
      Tarantula tarantula2 = tarantulaBean.create();
      assert tarantula != null;
      assert tarantula2 != null;
      assert tarantula != tarantula2;
   }
   
   @Test(groups={"stub", "contexts", "observerMethod"}) @SpecAssertion(section="8.3")
   public void testInstanceUsedForObserverMethodNotShared()
   {
      assert false;
   }
   
   @Test(groups="contexts") @SpecAssertion(section="8.3")
   public void testContextGetWithCreateTrueReturnsNewInstance()
   {
      Bean<Fox> foxBean = createSimpleBean(Fox.class);
      manager.addBean(foxBean);
      DependentContext context = new DependentContext();
      context.setActive(true);
      assert context.get(foxBean, true) != null;
      assert context.get(foxBean, true) instanceof Fox;
   }
   
   @Test(groups="contexts") @SpecAssertion(section="8.3")
   public void testContextGetWithCreateFalseReturnsNull()
   {
      Bean<Fox> foxBean = createSimpleBean(Fox.class);
      manager.addBean(foxBean);
      DependentContext context = new DependentContext();
      context.setActive(true);
      assert context.get(foxBean, false) == null;
   }
   
   @Test(groups="contexts", expectedExceptions=ContextNotActiveException.class) @SpecAssertion(section="8.3")
   public void testContextIsInactive()
   {
      manager.getContext(Dependent.class).isActive();
   }
   
   @Test(groups={"stub", "contexts", "observerMethod"}) @SpecAssertion(section="8.3")
   public void testContextIsActiveWhenInvokingObserverMethod()
   {
      assert false;
   }
   
   
   @Test(groups={"stub", "contexts", "el"}) @SpecAssertion(section="8.3")
   public void testContextIsActiveWhenEvaluatingElExpression()
   {
      assert false;
   }
   
   @Test(groups={"contexts", "beanLifecycle"}) @SpecAssertion(section="8.3")
   public void testContextIsActiveDuringBeanCreation()
   {
      // Slightly roundabout, but I can't see a better way to test atm
      Bean<FoxRun> foxRunBean = createSimpleBean(FoxRun.class);
      Bean<Fox> foxBean = createSimpleBean(Fox.class);
      manager.addBean(foxBean);
      FoxRun foxRun = foxRunBean.create();
      assert foxRun.fox != null;
   }
   
   @Test(groups={"stub", "contexts", "beanDestruction"}) @SpecAssertion(section="8.3")
   public void testContextIsActiveDuringBeanDestruction()
   {
      assert false;
   }
   
   @Test(groups={"contexts", "injection"}) @SpecAssertion(section="8.3")
   public void testContextIsActiveDuringInjection()
   {
      Bean<FoxRun> foxRunBean = createSimpleBean(FoxRun.class);
      Bean<Fox> foxBean = createSimpleBean(Fox.class);
      manager.addBean(foxBean);
      FoxRun foxRun = foxRunBean.create();
      assert foxRun.fox != null;
   }
   
   @Test(groups={"stub", "contexts", "ejb3"}) @SpecAssertion(section="8.3")
   public void testEjbBeanMayMayCreateInstanceFromInitializer()
   {
      assert false;
   }
   
   @Test(groups={"stub", "contexts", "ejb3"}) @SpecAssertion(section="8.3")
   public void testEjbBeanMayMayCreateInstanceFromPostConstruct()
   {
      assert false;
   }
   
   @Test(groups={"stub", "contexts", "ejb3"}) @SpecAssertion(section="8.3")
   public void testEjbBeanMayMayCreateInstanceFromPreDestroy()
   {
      assert false;
   }
   
   @Test(groups={"stub", "contexts", "servlet"}) @SpecAssertion(section="8.3")
   public void testServletBeanMayMayCreateInstanceFromInitializer()
   {
      assert false;
   }
   
   @Test(groups={"stub", "contexts", "beanDestruction"}) @SpecAssertion(section="8.3")
   public void testDestroyingParentDestroysDependents()
   {
      assert false;
   }
   
   @Test(groups={"stub", "contexts", "ejb3"}) @SpecAssertion(section="8.3")
   public void testDestroyingEjbDestroysDependents()
   {
      assert false;
   }
   
   @Test(groups={"stub", "contexts", "servlet"}) @SpecAssertion(section="8.3")
   public void testDestroyingServletDestroysDependents()
   {
      assert false;
   }
   
   @Test(groups={"stub", "contexts", "el"}) @SpecAssertion(section="8.3")
   public void testDependentsDestroyedWhenElEvaluationCompletes()
   {
      assert false;
   }
   
   @Test(groups={"stub", "contexts", "observerMethod"}) @SpecAssertion(section="8.3")
   public void testDependentsDestroyedWhenObserverMethodEvaluationCompletes()
   {
      assert false;
   }
   
}
