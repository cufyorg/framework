/*
 *	Copyright 2020 Cufy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package cufy.lang;

import cufy.util.Objects;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An alternative representation for {@link Class classes}. This provides more data about the
 * targeted type. About the components and the wildclass of that class. The wildclass is how the
 * class should be treated. The componentType is a component of types for each specific object in an
 * instance this type is representing.
 * <br>
 * Note: the class {@link Void} is the class of null.
 * <br>
 * Also Note: any recursions has been handled in this class (inner classes not included).
 *
 * @param <T> the type of the class represented by this type.
 * @author LSafer
 * @version 0.1.5
 * @since 0.1.0 ~2020.03.29
 */
public final class Type<T> implements java.lang.reflect.Type, Serializable {
	/**
	 * A pattern to validate {@link #getName() names} of {@code Component}s.
	 *
	 * @since 0.1.5 ~2020.08.11
	 */
	public static final Pattern COMPONENTS_PATTERN = Pattern.compile("^(?:[^,:<>*]+(?::[^,:<>*]+)?[*]?(?:<(?:[^,<>]*(?:<.*>)?,?)*>)?(?:,(?=\\s*\\S))?)*$");
	/**
	 * A pattern to split {@link #getName() names} of {@code Component}s.
	 *
	 * @since 0.1.5 ~2020.08.11
	 */
	public static final Pattern COMPONENTS_SPLIT = Pattern.compile("[^:,<>]+(?::[^:,<>]+)?(?:<(?:[^,<>]*(?:<.*>)?,?)*>)?(?=,(?=\\s*[^>]))?");
	/**
	 * The pattern to validate a {@link #getName() name} of a {@link Type}.
	 * <pre>
	 *     Use the group {@code "TYPE"} to extract the {@link #typeclass}.
	 *     Use the group {@code "WILD"} to extract the {@link #wildclass}.
	 *     Use the group {@code "COMPONENTS"} to extract the {@link #components}.
	 * </pre>
	 *
	 * @since 0.1.5 ~2020.08.11
	 */
	public static final Pattern PATTERN = Pattern.compile("^(?<TYPE>[^,:<>*]+)(?::(?<WILD>[^,:<>*]+))?(?<OBJECT>[*])?(?:<(?<COMPONENTS>(?:[^,<>]*(?:<.*>)?,?)*)>)?\\s*$");

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3076231001299756142L;

	/**
	 * The components of this type.
	 *
	 * @since 0.1.5 ~2020.08.11
	 */
	private Type[] components;
	/**
	 * Mappings for other types for each specific instance.
	 *
	 * @since 0.1.5 ~2020.08.11
	 */
	private IdentityHashMap<Object, Type> objecttypes;
	/**
	 * The class represented by this type. This field should be treated as final field.
	 *
	 * @since 0.1.5 ~2020.08.11
	 */
	private Class<T> typeclass;
	/**
	 * The class that an instance of this type should be treated as if it was an instance of it.
	 * This field should be treated as final field.
	 *
	 * @since 0.1.5 ~2020.08.11
	 */
	private Class wildclass;

	/**
	 * Construct a new type without initializing it. It is illegal to construct a new {@link Type}
	 * but not initialize it before sharing it outside this class. yet, this class can't know such
	 * thing.
	 * <p>
	 * Note: this is a backdoor constructor only to be utilized by this class.
	 *
	 * @since 0.1.5 ~2020.08.11
	 */
	private Type() {
	}

	/**
	 * Create a new array of types from the given {@code array}.
	 *
	 * @param array the source array.
	 * @return a new array of types from the given {@code array}. Or null if the given {@code array}
	 * 		is null.
	 * @since 0.1.5 ~2020.08.11
	 */
	public static Type[] array(Class... array) {
		if (array == null)
			return null;

		Type[] product = new Type[array.length];

		for (int i = 0; i < product.length; i++) {
			Class element = array[i];

			if (element != null)
				product[i] = Type.of(element);
		}

		return product;
	}

	/**
	 * Get a Class object associated with the class with the given {@code name}, Using the given
	 * {@code loader}.
	 * <pre>
	 *     "boolean" -> boolean.class
	 *     "byte" -> byte.class
	 *     "char" -> char.class
	 *     "double" -> double.class
	 *     "float" -> float.class
	 *     "int" -> int.class
	 *     "long" -> long.class
	 *     "short" -> short.class
	 * </pre>
	 *
	 * @param name       the name of the targeted class.
	 * @param initialize if {@code true} the any non-initialized class will be initialized.
	 * @param loader     class loader from which the classes must be loaded.
	 * @return ass object associated with the class with the given {@code name}.
	 * @throws ClassNotFoundException      if no type can be associated to the given {@code name}.
	 * @throws LinkageError                if the linkage failed.
	 * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
	 * @throws NullPointerException        if the given {@code name} or {@code loader} is null.
	 * @see Class#forName(String, boolean, ClassLoader)
	 * @since 0.1.5 ~2020.08.11
	 */
	public static Class classForName(String name, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(loader, "loader");
		switch (name) {
			case "boolean":
				return boolean.class;
			case "byte":
				return byte.class;
			case "char":
				return char.class;
			case "double":
				return double.class;
			case "float":
				return float.class;
			case "int":
				return int.class;
			case "long":
				return long.class;
			case "short":
				return short.class;
			default:
				return Class.forName(name, initialize, loader);
		}
	}

	/**
	 * Get a Class object associated with the class with the given {@code name}.
	 * <pre>
	 *     "boolean" -> boolean.class
	 *     "byte" -> byte.class
	 *     "char" -> char.class
	 *     "double" -> double.class
	 *     "float" -> float.class
	 *     "int" -> int.class
	 *     "long" -> long.class
	 *     "short" -> short.class
	 * </pre>
	 *
	 * @param name the name of the targeted class.
	 * @return ass object associated with the class with the given {@code name}.
	 * @throws LinkageError                if the linkage failed.
	 * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
	 * @throws ClassNotFoundException      if no type can be associated to the given {@code name}.
	 * @throws NullPointerException        if the given {@code name} is null.
	 * @see Class#forName(String)
	 * @since 0.1.5 ~2020.08.11
	 */
	public static Class classForName(String name) throws ClassNotFoundException {
		Objects.requireNonNull(name, "name");
		switch (name) {
			case "boolean":
				return boolean.class;
			case "byte":
				return byte.class;
			case "char":
				return char.class;
			case "double":
				return double.class;
			case "float":
				return float.class;
			case "int":
				return int.class;
			case "long":
				return long.class;
			case "short":
				return short.class;
			default:
				return Class.forName(name);
		}
	}

	/**
	 * Get a type that represents the {@code typeclass} of the given {@code typeclassSrc}, and have
	 * the {@code components} of the given {@code componentsSrc}, and should be treated as if it was
	 * the {@code wildclass} of the given {@code wildclassSrc}.
	 * <pre>
	 *     Type.of(<font color="a5edff">TYPE_SRC</font>, <font color="fc8fbb">WILD_SRC</font>, <font color="#bea341">OBJECT_SRC</font>, <font color="d3c4ff">COMPONENTS_SRC</font>)
	 *     <font color="a5edff">TYPE</font>:<font color="fc8fbb">WILD</font>&lt;<font color="d3c4ff">COMPONENTS…</font>&gt;
	 * </pre>
	 *
	 * @param typeclassSrc   the source of the class to be represented by the returned type.
	 * @param wildclassSrc   the source of the class that an instance of the returned type should be
	 *                       treated as if it was an instance of it.
	 * @param objecttypesSrc the source of the mappings for other types for each specific instance.
	 * @param componentsSrc  the source of the array of components of the returned type.
	 * @param <T>            the type of the class represented by the returned type.
	 * @return a type that represents the {@code typeclass} of the given {@code typeclassSrc}, and
	 * 		have the {@code component} of the given {@code componentsSrc}, and should be treated as if
	 * 		it was the {@code wildclass} of the given {@code wildclassSrc}.
	 * @throws NullPointerException if the given {@code wildclassSrc} or {@code typeclassSrc} or
	 *                              {@code componentsSrc} is null.
	 * @since 0.1.5 ~2020.08.11
	 */
	public static <T> Type<T> combine(Type<T> typeclassSrc, Type wildclassSrc, Type objecttypesSrc, Type componentsSrc) {
		Objects.requireNonNull(typeclassSrc, "typeclassSrc");
		Objects.requireNonNull(wildclassSrc, "wildclassSrc");
		Objects.requireNonNull(objecttypesSrc, "objecttypesSrc");
		Objects.requireNonNull(componentsSrc, "componentsSrc");
		Type type = new Type();
		type.typeclass = typeclassSrc.typeclass;
		type.wildclass = wildclassSrc.wildclass;
		type.objecttypes = objecttypesSrc.objecttypes;
		type.components = componentsSrc.components;
		return type;
	}

	/**
	 * Get a Type object associated with the type with the given {@code name}.
	 *
	 * @param name the fully qualified name of the desired type.
	 * @return a Type object for the type with the given {@code name}.
	 * @throws LinkageError                if the linkage failed.
	 * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
	 * @throws ClassNotFoundException      if no type can be associated to the given {@code name}.
	 * @throws NullPointerException        if the given {@code name} is null.
	 * @see Class#forName(String)
	 * @since 0.1.5 ~2020.08.11
	 */
	public static Type<?> forName(String name) throws ClassNotFoundException {
		Objects.requireNonNull(name, "name");
		Matcher matcher = Type.PATTERN.matcher(name);

		if (!matcher.find())
			throw new ClassNotFoundException(name);

		String typeString = matcher.group("TYPE");
		String wildString = matcher.group("WILD");
		String componentsString = matcher.group("COMPONENTS");

		Class typeclass = Type.classForName(typeString.trim());
		Class wildclass = wildString == null ?
						  typeclass :
						  Type.classForName(wildString.trim());
		Type[] components = componentsString == null ?
							new Type[0] :
							Type.forNames(componentsString);

		Type type = new Type();
		type.typeclass = typeclass;
		type.wildclass = wildclass;
		type.objecttypes = new IdentityHashMap(0);
		type.components = components;
		return type;
	}

	/**
	 * Get a Type object associated with the type with the given {@code name}, Using the given
	 * {@code loader}.
	 *
	 * @param name       the fully qualified name of the desired component.
	 * @param initialize if {@code true} the any non-initialized class will be initialized.
	 * @param loader     class loader from which the classes must be loaded.
	 * @return a Type object for the type with the given {@code name}.
	 * @throws LinkageError                if the linkage failed.
	 * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
	 * @throws ClassNotFoundException      if no type can be associated to the given {@code name}.
	 * @throws NullPointerException        if the given {@code name} or {@code loader} is null.
	 * @see Class#forName(String, boolean, ClassLoader)
	 * @since 0.1.5 ~2020.08.11
	 */
	public static Type<?> forName(String name, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(loader, "loader");
		Matcher matcher = Type.PATTERN.matcher(name);

		if (!matcher.find())
			throw new ClassNotFoundException(name);

		String typeString = matcher.group("TYPE");
		String wildString = matcher.group("WILD");
		String componentsString = matcher.group("COMPONENTS");

		Class typeclass = Type.classForName(typeString.trim(), initialize, loader);
		Class wildclass = wildString == null ?
						  typeclass :
						  Type.classForName(wildString.trim(), initialize, loader);
		Type[] components = componentsString == null ?
							new Type[0] :
							Type.forNames(componentsString, initialize, loader);

		Type type = new Type();
		type.typeclass = typeclass;
		type.wildclass = wildclass;
		type.objecttypes = new IdentityHashMap(0);
		type.components = components;
		return type;
	}

	/**
	 * Get a Types array associated with the types with the given {@code names}.
	 *
	 * @param names the fully qualified names of the desired types array.
	 * @return a Types array for the types with the given {@code name}.
	 * @throws LinkageError                if the linkage failed.
	 * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
	 * @throws ClassNotFoundException      if no types can be associated to the given {@code name}.
	 * @throws NullPointerException        if the given {@code name} is null.
	 * @since 0.1.5 ~2020.08.11
	 */
	public static Type[] forNames(String names) throws ClassNotFoundException {
		Objects.requireNonNull(names, "names");

		if (!Type.COMPONENTS_PATTERN.matcher(names).matches())
			throw new ClassNotFoundException(names);

		List<Type> list = new ArrayList();

		Matcher matcher = Type.COMPONENTS_SPLIT.matcher(names.trim());
		while (matcher.find()) {
			String componentName = matcher.group().trim();
			list.add(
					"?".equals(componentName) ?
					null :
					Type.forName(componentName)
			);
		}

		return list.toArray(new Type[0]);
	}

	/**
	 * Get a Types array associated with the types with the given {@code names}. Using the given
	 * {@code loader}.
	 *
	 * @param names      the fully qualified names of the desired types array.
	 * @param initialize if {@code true} the any non-initialized class will be initialized.
	 * @param loader     class loader from which the classes must be loaded.
	 * @return a Types array for the types with the given {@code name}.
	 * @throws LinkageError                if the linkage failed.
	 * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
	 * @throws ClassNotFoundException      if no types can be associated to the given {@code name}.
	 * @throws NullPointerException        if the given {@code name} or {@code loader} is null.
	 * @since 0.1.5 ~2020.08.11
	 */
	public static Type[] forNames(String names, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
		Objects.requireNonNull(names, "names");
		Objects.requireNonNull(loader, "loader");

		if (!Type.COMPONENTS_PATTERN.matcher(names).matches())
			throw new ClassNotFoundException(names);

		List<Type> list = new ArrayList();

		Matcher matcher = Type.COMPONENTS_SPLIT.matcher(names.trim());
		while (matcher.find()) {
			String componentName = matcher.group().trim();
			list.add(
					"?".equals(componentName) ?
					null :
					Type.forName(componentName, initialize, loader)
			);
		}

		return list.toArray(new Type[0]);
	}

	/**
	 * Create a new map of objects and types from the given {@code map}.
	 *
	 * @param map the source map.
	 * @return a new map of objects and types from the given {@code map}. Or null if the given
	 *        {@code map} is null.
	 * @throws ClassCastException if the given {@code map} has a value that is not an instance of
	 *                            {@link Class}.
	 * @since 0.1.5 ~2020.08.11
	 */
	public static Map<Object, Type> map(Map<Object, Class> map) {
		if (map == null)
			return null;

		Map product = new IdentityHashMap(map.size());

		for (Map.Entry<Object, Class> entry : map.entrySet()) {
			Object key = entry.getKey();
			Class value = entry.getValue();

			if (value != null)
				product.put(key, Type.of(value));
		}

		return product;
	}

	/**
	 * Get a type that represents the given {@code typeclass}, and have the given {@code
	 * components}.
	 * <pre>
	 *     Type.ofc(<font color="a5edff">TYPE</font>, <font color="d3c4ff">COMPONENTS…</font>)
	 *     <font color="a5edff">TYPE</font><font color="d3c4ff">&lt;COMPONENTS…&gt;</font>
	 * </pre>
	 *
	 * @param typeclass  the class to be represented by the returned type.
	 * @param components the components of the returned type.
	 * @param <T>        the type of the class represented by the returned type.
	 * @return a type that represents the given {@code typeclass}, and have the given {@code
	 * 		components}.
	 * @throws NullPointerException if the given {@code typeclass} or {@code components} is null.
	 * @since 0.1.5 ~2020.08.11
	 */
	public static <T> Type<T> of(Class<T> typeclass, Class... components) {
		Objects.requireNonNull(typeclass, "typeclass");
		Objects.requireNonNull(components, "components");
		Type<T> type = new Type();
		type.typeclass = typeclass;
		type.wildclass = typeclass;
		type.objecttypes = new IdentityHashMap(0);
		type.components = Type.array(components);
		return type;
	}

	/**
	 * Get a type that represents the given {@code typeclass}, and have the given {@code
	 * components}, and should be treated as if it was the given {@code wildclass}.
	 * <pre>
	 *     Type.ofw(<font color="a5edff">TYPE</font>, <font color="fc8fbb">WILD</font>, <font color="d3c4ff">COMPONENTS…</font>)
	 *     <font color="a5edff">TYPE</font><font color="fc8fbb">:WILD</font><font color="d3c4ff">&lt;COMPONENTS…&gt;</font>
	 * </pre>
	 *
	 * @param typeclass  the class to be represented by the returned type.
	 * @param wildclass  the class that an instance of the returned type should be treated as if it
	 *                   was an instance of it.
	 * @param components the components of the returned type.
	 * @param <T>        the type of the class represented by the returned type.
	 * @return a type that represents the given {@code typeclass}, and have the given {@code
	 * 		components}, and should be treated as if it was the given {@code wildclass}.
	 * @throws NullPointerException if the given {@code typeclass} or {@code wildclass} or {@code
	 *                              components} is null.
	 * @since 0.1.5 ~2020.08.11
	 */
	public static <T> Type<T> ofw(Class<T> typeclass, Class wildclass, Class... components) {
		Objects.requireNonNull(typeclass, "typeclass");
		Objects.requireNonNull(wildclass, "wildclass");
		Objects.requireNonNull(components, "component");
		Type<T> type = new Type();
		type.typeclass = typeclass;
		type.wildclass = wildclass;
		type.objecttypes = new IdentityHashMap(0);
		type.components = Type.array(components);
		return type;
	}

	@Override
	public boolean equals(Object object) {
		return object == this ||
			   object instanceof Type &&
			   this.equals0((Type) object, new IdentityHashMap());
	}

	@Override
	public String getTypeName() {
		StringBuilder builder = new StringBuilder();
		this.getTypeName0(builder, Collections.newSetFromMap(new IdentityHashMap()));
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return this.hashCode0(Collections.newSetFromMap(new IdentityHashMap()));
	}

	@Override
	public String toString() {
		return "type " + this.getName();
	}

	/**
	 * Construct a new builder with initial values from this type.
	 *
	 * @return a new builder with initial values from this type.
	 */
	public Builder<T> builder() {
		return this.builder0(new IdentityHashMap());
	}

	/**
	 * Get the component of this type that has the given {@code component} index.
	 *
	 * @param component the index of the targeted component.
	 * @return the component of this type that has the given {@code component} index. or null if no
	 * 		such component in this type with the given {@code component} index.
	 * @since 0.1.5 ~2020.08.11
	 */
	public Type getComponentType(int component) {
		return component >= 0 &&
			   component < this.components.length ?
			   this.components[component] :
			   null;
	}

	/**
	 * Get a clone of the array holding all the components of this type.
	 *
	 * @return a clone of the array holding all the components of this type.
	 * @since 0.1.5 ~2020.08.11
	 */
	public Type[] getComponentTypes() {
		return this.components.clone();
	}

	/**
	 * Get how many components this type has.
	 *
	 * @return the number of components this type has.
	 */
	public int getComponentsCount() {
		return this.components.length;
	}

	/**
	 * Get the name of this type.
	 * <pre>
	 *     Type.getName()
	 *     <font color="a5edff">TYPE</font><font color="fc8fbb">:WILD</font><font color="#bea341">*</font><font color="d3c4ff">&lt;COMPONENTS…&gt;</font>
	 * </pre>
	 * <pre>
	 *     Type.of(ArrayList.class, Set.class, Integer.class).getName()
	 *     <font color="a5edff">java.util.ArrayList</font><font color="fc8fbb">:java.util.Set</font><font color="d3c4ff">&lt;java.lang.Integer&gt;</font>
	 * </pre>
	 *
	 * @return the name of this type.
	 * @see Type#forName(String)
	 * @since 0.1.5 ~2020.08.11
	 */
	public String getName() {
		StringBuilder builder = new StringBuilder();
		this.getName0(builder, Collections.newSetFromMap(new IdentityHashMap()));
		return builder.toString();
	}

	/**
	 * Get the type instance of this type that has been specified for the given {@code object}.
	 *
	 * @param object the object to get the type instance of this that has been specified for it.
	 * @return the type instance of this type that has been specified for the given {@code object}.
	 * 		Or this type instance, if no type has been specified for the given {@code object}.
	 * @since 0.1.5 ~2020.08.11
	 */
	public Type getObjecttype(Object object) {
		Type type = this.objecttypes.get(object);
		return type == null ? this : type;
	}

	/**
	 * Get an unmodifiable view of the object-type mappings of this type.
	 *
	 * @return an unmodifiable view of the objecttypes of this type.
	 * @since 0.1.5 ~2020.08.11
	 */
	public Map<Object, Type> getObjecttypes() {
		return Collections.unmodifiableMap(this.objecttypes);
	}

	/**
	 * Get the simple name of this type. The name contains the simple name of the {@code wildclass},
	 * {@code typeclass}, and each {@code componentType} of each {@code component} in this type.
	 *
	 * @return the simple name of this type.
	 * @since 0.1.5 ~2020.08.11
	 */
	public String getSimpleName() {
		StringBuilder builder = new StringBuilder();
		this.getSimpleName0(builder, Collections.newSetFromMap(new IdentityHashMap()));
		return builder.toString();
	}

	/**
	 * Get the class represented by this type.
	 *
	 * @return the class represented by this type.
	 * @since 0.1.5 ~2020.08.11
	 */
	public Class<T> getTypeclass() {
		return this.typeclass;
	}

	/**
	 * Get the class that an instance of this type should be treated as if it was an instance of
	 * it.
	 *
	 * @return the class that an instance of this type should be treated as if it was an instance of
	 * 		it.
	 * @since 0.1.5 ~2020.08.11
	 */
	public Class getWildclass() {
		return this.wildclass;
	}

	/**
	 * Construct a new builder using the given {@code dejaVu} as a mapping for the results of
	 * previously constructed builders.
	 *
	 * @param dejaVu a map mapping the previously constructed builders.
	 * @return a builder from this type.
	 * @throws NullPointerException if the given {@code dejaVu} is null.
	 * @since 0.1.5 ~2020.08.11
	 */
	private Builder<T> builder0(Map<Type, Builder> dejaVu) {
		Objects.requireNonNull(dejaVu, "dejaVu");
		if (dejaVu.containsKey(this))
			return dejaVu.get(this);

		Builder builder = new Builder(this.typeclass, this.wildclass, new IdentityHashMap(), new ArrayList());
		dejaVu.put(this, builder);

		for (Map.Entry<Object, Type> entry : this.objecttypes.entrySet()) {
			Object key = entry.getKey();
			Type value = entry.getValue();

			if (value != null)
				builder.objecttypes.put(key, value.builder0(dejaVu));
		}

		for (Type component : this.components)
			builder.components.add(component == null ? null : component.builder0(dejaVu));

		return builder;
	}

	/**
	 * Match this type with the given {@code type}. Or return {@code true}, if the given {@code
	 * type} has been associated to this type in the given {@code dejaVu}.
	 *
	 * @param type   the type to be matched with this type.
	 * @param dejaVu a map associating the couples previously matched.
	 * @return true, if this type equals the given {@code type}.
	 * @throws NullPointerException if the given {@code dejaVu} is null.
	 * @since 0.1.5 ~2020.08.14
	 */
	private boolean equals0(Type<?> type, Map<Type, Set<Type>> dejaVu) {
		Objects.requireNonNull(dejaVu, "dejaVu");
		if (type == this ||
			dejaVu.containsKey(this) && dejaVu.get(this).contains(type) ||
			dejaVu.containsKey(type) && dejaVu.get(type).contains(this))
			return true;
		if (type.typeclass != this.typeclass ||
			type.wildclass != this.wildclass ||
			type.components.length != this.components.length ||
			type.objecttypes.size() != this.objecttypes.size())
			return false;

		Set<Type> dejaVus = dejaVu.computeIfAbsent(this, k -> Collections.newSetFromMap(new IdentityHashMap()));
		dejaVus.add(type);

		for (Map.Entry<Object, Type> entry : type.objecttypes.entrySet()) {
			Object object = entry.getKey();

			for (Map.Entry<Object, Type> e : this.objecttypes.entrySet()) {
				Object o = e.getKey();

				if (object == o) {
					Type objecttype = entry.getValue();
					Type ot = e.getValue();

					if (objecttype.equals0(ot, dejaVu))
						continue;

					break;
				}
			}

			return false;
		}

		for (int i = 0; i < type.components.length; i++) {
			Type component = type.components[i];
			Type c = this.components[i];

			if (component.equals0(c, dejaVu))
				continue;

			return false;
		}

		return true;
	}

	/**
	 * Append the name of this type to the given {@code builder}. Or append {@code "?"}, if this
	 * type contained in the given {@code dejaVu}.
	 *
	 * @param builder the builder to append the name of this type to.
	 * @param dejaVu  the types that has been seen before.
	 * @throws NullPointerException if the given {@code builder} or {@code dejaVu} is null.
	 * @since 0.1.5 ~2020.08.12
	 */
	private void getName0(StringBuilder builder, Set<Type> dejaVu) {
		Objects.requireNonNull(dejaVu, "dejaVu");
		if (dejaVu.contains(this)) {
			builder.append("?");
			return;
		}

		dejaVu.add(this);

		builder.append(this.typeclass.getName());

		if (this.wildclass != this.typeclass)
			builder.append(":")
					.append(this.wildclass.getName());

		if (!this.objecttypes.isEmpty())
			builder.append("*");

		if (this.components.length == 0)
			return;

		builder.append("<");

		int i = 0;
		while (true) {
			Type component = this.components[i];

			if (component == null)
				builder.append("?");
			else
				component.getName0(builder, dejaVu);

			if (++i < this.components.length) {
				builder.append(", ");
				continue;
			}

			builder.append(">");
			break;
		}

		dejaVu.remove(this);
	}

	/**
	 * Append the simple name of this type to the given {@code builder}. Or append {@code "?"}, if
	 * this type contained in the given {@code dejaVu}.
	 *
	 * @param builder the builder to append the simple name of this type to.
	 * @param dejaVu  the types that has been seen before.
	 * @throws NullPointerException if the given {@code builder} or {@code dejaVu} is null.
	 * @since 0.1.5 ~2020.08.12
	 */
	private void getSimpleName0(StringBuilder builder, Set<Type> dejaVu) {
		Objects.requireNonNull(builder, "builder");
		Objects.requireNonNull(dejaVu, "dejaVu");
		if (dejaVu.contains(this)) {
			builder.append("?");
			return;
		}

		dejaVu.add(this);

		builder.append(this.typeclass.getSimpleName());

		if (this.wildclass != this.typeclass)
			builder.append(":")
					.append(this.wildclass.getSimpleName());

		if (!this.objecttypes.isEmpty())
			builder.append("*");

		if (this.components.length == 0)
			return;

		builder.append("<");

		int i = 0;
		while (true) {
			Type component = this.components[i];

			if (component == null)
				builder.append("?");
			else
				component.getSimpleName0(builder, dejaVu);

			if (++i < this.components.length) {
				builder.append(", ");
				continue;
			}

			builder.append(">");
			break;
		}

		dejaVu.remove(this);
	}

	/**
	 * Append the type name of this type to the given {@code builder}. Or append {@code "?"}, if
	 * this type contained in the given {@code dejaVu}.
	 *
	 * @param builder the builder to append the type name of this type to.
	 * @param dejaVu  the types that has been seen before.
	 * @throws NullPointerException if the given {@code builder} or {@code dejaVu} is null.
	 * @since 0.1.5 ~2020.08.12
	 */
	private void getTypeName0(StringBuilder builder, Set<Type> dejaVu) {
		Objects.requireNonNull(builder, "builder");
		Objects.requireNonNull(dejaVu, "dejaVu");
		if (dejaVu.contains(this)) {
			builder.append("?");
			return;
		}

		dejaVu.add(this);

		builder.append(this.typeclass.getTypeName());

		if (this.wildclass != this.typeclass)
			builder.append(":")
					.append(this.wildclass.getTypeName());

		if (!this.objecttypes.isEmpty())
			builder.append("*");

		if (this.components.length == 0)
			return;

		builder.append("<");

		int i = 0;
		while (true) {
			Type component = this.components[i];

			if (component == null)
				builder.append("?");
			else
				component.getTypeName0(builder, dejaVu);

			if (++i < this.components.length) {
				builder.append(", ");
				continue;
			}

			builder.append(">");
			break;
		}

		dejaVu.remove(this);
	}

	/**
	 * Calculate the hash code of this type. Or return {@code 1}, If this type contained in the
	 * given {@code dejaVu}.
	 *
	 * @param dejaVu the types that has been seen before.
	 * @return the hash code of this type.
	 * @throws NullPointerException if the given {@code dejaVu} is null.
	 * @since 0.1.5 ~2020.08.14
	 */
	private int hashCode0(Set<Type> dejaVu) {
		Objects.requireNonNull(dejaVu, "dejaVu");
		if (dejaVu.contains(this))
			return 1;

		dejaVu.add(this);

		int hashCode = this.typeclass.hashCode();

		if (this.typeclass != this.wildclass)
			hashCode ^= this.wildclass.hashCode();

		for (Map.Entry<Object, Type> entry : this.objecttypes.entrySet()) {
			Object object = entry.getKey();
			Type objecttype = entry.getValue();
			hashCode += objecttype == null ? 0 :
						System.identityHashCode(object) ^
						objecttype.hashCode0(dejaVu);
		}

		for (Type component : this.components)
			hashCode = 31 * hashCode + (component == null ? 0 : component.hashCode0(dejaVu));

		dejaVu.remove(this);
		return hashCode;
	}

	/**
	 * A builder to allow taking full control over what a type should be like more easily and
	 * securely. Since the {@link Type} class should be a reliably constant type representation.
	 *
	 * @param <T> the type of the typeclass.
	 * @author LSafer
	 * @version 0.1.5
	 * @since 0.1.5 ~2020.08.11
	 */
	public static class Builder<T> implements Serializable {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 3541772414490825518L;

		/**
		 * The {@link Type#components} of the type returned by the next calls to {@link #build()}.
		 *
		 * @since 0.1.5 ~2020.08.11
		 */
		public List<Builder> components;
		/**
		 * The {@link Type#objecttypes} of the type returned by the next calls to {@link #build()}.
		 *
		 * @since 0.1.5 ~2020.08.11
		 */
		public Map<Object, Builder> objecttypes;
		/**
		 * The {@link Type#typeclass} of the type returned by the next calls to {@link #build()}.
		 *
		 * @since 0.1.5 ~2020.08.11
		 */
		public Class<T> typeclass;
		/**
		 * The {@link Type#wildclass} of the type returned by the next calls to {@link #build()}.
		 *
		 * @since 0.1.5 ~2020.08.11
		 */
		public Class wildclass;

		/**
		 * Construct a new builder with the default variables.
		 *
		 * @since 0.1.5 ~2020.08.11
		 */
		public Builder() {
			this.typeclass = (Class) Object.class;
			this.wildclass = Object.class;
			this.objecttypes = new IdentityHashMap();
			this.components = new ArrayList();
		}

		/**
		 * Construct a new builder with the given parameters as the initial values of its fields.
		 *
		 * @param typeclass   the initial typeclass that the constructed builder will have.
		 * @param wildclass   the initial wildclass that the constructed builder will have.
		 * @param objecttypes the initial objecttypes map that the constructed builder will have.
		 * @param components  the initial components list that the constructed builder will have.
		 * @since 0.1.5 ~2020.08.14
		 */
		public Builder(Class<T> typeclass, Class wildclass, Map<Object, Builder> objecttypes, List<Builder> components) {
			this.typeclass = typeclass;
			this.wildclass = wildclass;
			this.objecttypes = objecttypes;
			this.components = components;
		}

		@Override
		public boolean equals(Object object) {
			return this == object ||
				   object instanceof Builder &&
				   Objects.equals(((Builder) object).typeclass, this.typeclass) &&
				   Objects.equals(((Builder) object).wildclass, this.wildclass) &&
				   Objects.equals(((Builder) object).objecttypes, this.objecttypes) &&
				   Objects.equals(((Builder) object).components, this.components);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(this.wildclass) ^
				   Objects.hashCode(this.typeclass) ^
				   Objects.hashCode(this.objecttypes) ^
				   Objects.hashCode(this.components);
		}

		@Override
		public String toString() {
			return "Builder{" +
				   "typeclass=" + this.typeclass +
				   ", wildclass=" + this.wildclass +
				   ", objecttypes=" + this.objecttypes +
				   ", components=" + this.components +
				   "}";
		}

		/**
		 * Build this builder.
		 *
		 * @return a type from building this builder.
		 * @throws ClassCastException if {@link Builder#objecttypes} has a value that is not an
		 *                            instance of {@link Builder}.
		 * @since 0.1.5 ~2020.08.11
		 */
		public Type<T> build() {
			return this.build0(new IdentityHashMap());
		}

		/**
		 * Get the current set source for the components.
		 *
		 * @return the current set source for the components.
		 * @since 0.1.5 ~2020.08.11
		 */
		public List<Builder> getComponents() {
			return this.components;
		}

		/**
		 * Get the current set source for the objecttypes.
		 *
		 * @return the current set source for the objecttypes.
		 * @since 0.1.5 ~2020.08.11
		 */
		public Map<Object, Builder> getObjecttypes() {
			return this.objecttypes;
		}

		/**
		 * Get the current set typeclass.
		 *
		 * @return the current set typeclass.
		 * @since 0.1.5 ~2020.08.11
		 */
		public Class<T> getTypeclass() {
			return this.typeclass;
		}

		/**
		 * Get the current set wildclass.
		 *
		 * @return the current set wildclass.
		 * @since 0.1.5 ~2020.08.11
		 */
		public Class getWildclass() {
			return this.wildclass;
		}

		/**
		 * Set the {@link Type#components} for the type returned from the next {@link #build()}
		 * calls to be the result of computing the given {@code components}.
		 *
		 * @param components the source components for the components to be set.
		 * @return this.
		 * @since 0.1.5 ~2020.08.11
		 */
		public Builder<T> setComponents(List<Builder> components) {
			this.components = components;
			return this;
		}

		/**
		 * Set the {@link Type#objecttypes} for the type returned from the next {@link #build()}
		 * calls to be the result of computing the given {@code objecttypes}.
		 *
		 * @param objecttypes the source objecttypes for the objecttypes to be set.
		 * @return this.
		 * @since 0.1.5 ~2020.08.11
		 */
		public Builder<T> setObjecttypes(Map<Object, Builder> objecttypes) {
			this.objecttypes = objecttypes;
			return this;
		}

		/**
		 * Set the {@link Type#typeclass} for the type returned from the next {@link #build()} calls
		 * to be the given {@code typeclass}.
		 *
		 * @param typeclass the typeclass to be set.
		 * @return this.
		 * @since 0.1.5 ~2020.08.11
		 */
		public Builder<T> setTypeclass(Class<T> typeclass) {
			this.typeclass = typeclass;
			return this;
		}

		/**
		 * Set the {@link Type#wildclass} for the type returned from the next {@link #build()} calls
		 * to be the given {@code wildclass}.
		 *
		 * @param wildclass the wildclass to be set.
		 * @return this.
		 * @since 0.1.5 ~2020.08.11
		 */
		public Builder<T> setWildclass(Class wildclass) {
			this.wildclass = wildclass;
			return this;
		}

		/**
		 * Build this builder using the given {@code dejaVu} as a mapping for the results of
		 * previously built builders.
		 *
		 * @param dejaVu a map mapping the previously built builders and their results.
		 * @return a type from building this builder.
		 * @throws NullPointerException if the given {@code dejaVu} is null.
		 * @throws ClassCastException   if {@link Builder#objecttypes} has a value that is not an
		 *                              instance of {@link Builder}.
		 * @since 0.1.5 ~2020.08.11
		 */
		private Type<T> build0(Map<Builder, Type> dejaVu) {
			Objects.requireNonNull(dejaVu, "dejaVu");
			if (dejaVu.containsKey(this))
				return dejaVu.get(this);

			Type type = new Type();
			dejaVu.put(this, type);

			//capture
			Class builderTypeclass = this.typeclass;
			Class builderWildclass = this.wildclass;
			Map<Object, Builder> builderObjecttypes = this.objecttypes;
			List<Builder> builderComponents = this.components;

			//compute typeclass
			Class typeclass;
			if (builderTypeclass == null)
				typeclass = Object.class;
			else
				typeclass = builderTypeclass;

			//compute wildclass
			Class wildclass;
			if (builderWildclass == null)
				wildclass = typeclass;
			else
				wildclass = builderWildclass;

			//compute objecttypes
			IdentityHashMap<Object, Type> objecttypes;
			if (builderObjecttypes == null)
				objecttypes = new IdentityHashMap(0);
			else {
				objecttypes = new IdentityHashMap(builderObjecttypes.size());

				for (Map.Entry<Object, Builder> entry : builderObjecttypes.entrySet()) {
					Object key = entry.getKey();
					Builder value = entry.getValue();

					if (value != null)
						objecttypes.put(key, value.build0(dejaVu));
				}
			}

			//compute components
			Type[] components;
			if (builderComponents == null)
				components = new Type[0];
			else {
				components = new Type[builderComponents.size()];

				for (int i = 0; i < components.length; i++) {
					Builder element = builderComponents.get(i);

					if (element != null)
						components[i] = element.build0(dejaVu);
				}
			}

			type.typeclass = typeclass;
			type.wildclass = wildclass;
			type.objecttypes = objecttypes;
			type.components = components;
			return type;
		}
	}
}
