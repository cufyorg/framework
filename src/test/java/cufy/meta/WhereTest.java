package cufy.meta;

import org.junit.Test;

@SuppressWarnings("JavaDoc")
public class WhereTest {
	@Test
	@Where(
			value = SomeWhere.class,
			name = "here"
	)
	public void docs() {
		//this will give us the location of the field above
	}

	class SomeWhere {
		//the id tells exactly what field we are looking for
		@Where.Target(name = "here")
		public int myField;
	}
}
