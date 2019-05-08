# 클래스, 객체, 인터페이스

코틀린의 클래스와 인터페이스는 자바 클래스, 인터페이스와는 약간 다르다.

* 인터페이스 내 프로퍼티 선언이 들어갈 수 있다.
* 코틀린 선언은 기본적으로 final 이며 public 이다.
* 중첩 클래스는 기본적으로 내부 클래스가 아니다. 즉 코틀린 중첩 클래스에는 외부 클래스에 대한 참조가 없다.
* 클래스를 data 로 선언하면 컴파일러가 일부 표준 메소드를 생성해준다.
* 코틀린 언어가 제공하는 위임을 사용하면 위임을 처리하기 위한 준비 메소드를 직접 작성할 필요가 없다.
* 싱글턴 클래스, 동반 객체, 객체 식(자바의 무명클래스)를 표현할 때 object 키워드를 쓴다.



### 4.1 클래스 계층 정의

**4.1.1 코틀린 인터페이스**

코틀린 인터페이스는 자바 8 인터페이스와 비슷하다

```kotlin
//코틀린 인터페이스 안에는 추상 메소드뿐 아니라 구현이 있는 메소드도 정의할 수 있다.
interface Clickable {
    fun click()

    //디폴트 구현이 있는 메소드
    fun showOff() = println("I'm clickable!")
}

//동일한 메소드를 구현하는 다른 인터페이스 정의
interface Focusable {
    fun setFocus(b: Boolean) =
        println("I ${if (b) "got" else "lost"} focus")

    //Clickable과 같은 showOff 메소드를 가지고 있다.
    //같이 구현할 경우 컴파일 에러가 발생한다.
    fun showOff() = println ("I'm focusable!")
}

class Button: Clickable {
    override fun click() = println("I was clicked")
}

class Button2: Clickable, Focusable {
    override fun showOff() {
        //상위 타입의 구현을 호출할 때는 자바와 마찬가지로 super 을 사용한다.
        super<Clickable>.showOff()
        super<Focusable>.showOff()
    }

    override fun click() = println("I was clicked 2")
}
```

**4.1.2 open, final, abstract 변경자: 기본적으로 final**

**취약한 기반 클래스라는 문제**는 하위 클래스가 기반 클래스에 대해 가졌던 가정이 기반 클래스를 변경함으로써 져버린 경우에 생긴다.<br>
어떤 클래스가 자신을 상속하는 방법에 대해 정확한 규칙을 제공하지 않는다면 그 클래스의 클라이언트는 기반 클래스를 작성한 사람의 의도와 다른 방식으로 메소드를 오버라이드할 위험이 있다.모든 하위 클래스르 분석하는 것은 불가능하므로 기반 클래스는 취약하다<br><br>

자바의 클래스와 메소드는 기본적으로 상속에 대해 열려있지만 코틀린의 클래스와 메소드는 기본적으로 final 이다.

 * final -> 오버라이드할 수 없음
 * open -> 오버라이드할 수 있음
 * abstract -> 반드시 오버라이드해야 함
 * override -> 상위 클래스나 상위 인스턴스의 멤버를 오버라이드하는 중

**4.1.3 가시성 변경자: 기본적으로 공개**

 * public -> 모든 곳에서 볼 수 있다.
 * internal -> 같은 모듈 안에서만 볼 수 있다.
 * protected -> 하위 클래스 안에서만 볼 수 있다.
 * private -> 같은 클래스 안에서만 볼 수 있다.

**4.1.4 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스**

중첨 클래스는 기본적으로 내부 클래스가 아니다. 바깥쪽 클래스에 대한 참조를 중첩 클래스 안에 포함시키려면 inner 키워드를 중첩 클래스 선언 앞에 붙여서 내부 클래스로 만들어야 한다.

```kotlin
class Outer {

    inner class inner {
        //내부 클래스 안에서 바깥쪽 클래스 참조에 전급하려면 this@Outer 라고 써야 한다.
        fun getOuterReference(): Outer = this@Outer
    }
}
```

**4.1.5 봉인된 클래스: 클래스 계층 정의 시 계층 확장 제한**

- 상위 클래스에 sealed 변경자를 붙이면 그 상위 클래스를 상속한 하위 클래스 정의를 제한할 수 있다.
- sealed 클래스의 하위 클래스를 정의할 때는 반드시 상위 클래스 안에 중첩시켜야 한다.

```kotlin
interface Expr1

class Num(val value: Int) : Expr1

class Sum(val left: Expr1, val right: Expr1) : Expr1

fun eval1(e: Expr1) : Int =
    when(e) {
        //else 분기가 꼭 있어야 한다.
        is Num -> e.value
        is Sum -> eval1(e.right) + eval1(e.left)
        else ->
            throw IllegalArgumentException("Unknown expression")
    }

sealed class Expr2 {
    class Num(val value:Int) : Expr2()

    class Sum(val left: Expr2, val right: Expr2) : Expr2()
}

fun eval2(e: Expr2) : Int =
        when(e) {
            //when 식이 모든 하위 클래스를 검사하므로 별도의 else 분기가 없어도 된다.
            is Expr2.Num -> e.value
            is Expr2.Sum -> eval2(e.right) + eval2(e.left)
        }
```



### 4.2 뻔하지 않은 생성자와 프로퍼티를 갖는 클래스 선언

**4.2.1 클래스 초기화: 주 생성자와 초기화 블록**

클래스 이름 뒤에 괄호로 둘러싸인 코드를 주 생성자라고 부른다.

```kotlin
class User1(val nickname: String)
```

- constructor은 주 생성자나 부 생성자 정의를 시작할 때 사용한다.
- init 키워드는 초기화 블록을 시작한다.
- 초기화 블록에는 클래스의 객체가 만들어질 때 실행될 초기화 코드가 들어간다.

```kotlin
class User2 constructor(_nickname: String) {
    val nickname: String

    init {
        nickname = _nickname
    }
}
```

- 클래스를 상속한 하위 클래스는 반드시 상위 클래스의 생성자를 호출해야 한다.
- 이 규칙으로 인해 기반 클래스의 이름 뒤에는 꼭 빈 괄호가 들어간다(물론 생성자 인자가 있다면 괄호 안에 인자가 들어간다)
- 반면 인터페이스는 생성자가 없기 때문에 어떤 클래스가 인터페이스를 구현하는 경우 그 클래스의 상위 클래스 목록에 있는 인터페이스 목록에서 이름 뒤에 괄호가 붙었는지 살펴보면 쉽게 기반 클래스와 인터페이스를 구별할 수 있다.

**4.2.2 부 생성자: 상위 클래스를 다른 방식으로 초기화**

**팁**
인자에 대한 디폴트 값을 제공하기 위해 부 생성자를 여럿 만들지 말라. 대신 파라미터의 디폴트 값을 생성자 시그니처에 직접 명시하라

```kotlin
open class View {
    constructor(ctx: Context) {

    }
    constructor(ctx: Context, attr: AttributeSet) {

    }
}
```
이 클래스는 주 생성자를 선언하지 않고 부 생성자만 2가지 선언한다. 부 생성자는 constructor 키워드로 시작한다. 필요에 따라 얼마든지 부 생성자를 많이 선언해도 된다.



```kotlin
class MyButton : View {
    constructor(ctx: Context)
            : super(ctx) { //상위 클래스의 생성자를 호출한다.

    }

    constructor(ctx: Context, attr: AttributeSet)
            : super(ctx, attr) {

    }
}
```
여기서 두 부 생성자는 super() 키워드를 통해 자신에 대응하는 상위 클래스 생성자를 호출한다. 즉 생성자가 상위 클래스 생성자에게 객체 생성을 위힘한다는 사실을 표시한다.
자바와 마찬가지로 생성자에서 this()를 통해 클래스 자신의 다른 생성자를 호출할 수 있다.

```kotlin
class MyButton : View {
    constructor(ctx: Context)
            : this(ctx, MY_STYLE) {

    }

    constructor(ctx: Context, attr: AttributeSet)
            : super(ctx, attr) {

    }
}
```
디폴트(MY_STYLE) 값을 넘겨서 같은 클래스의 다른 생성자에게 생성을 위임한다. 두번 째 생성자는 여전히 super()를 호출한다.

클래스에 주 생성자가 없다면 모든 부 생성자는 반드시 상위 클래스를 초기화하거나 다른 생성자에게 생성을 위임해야 한다.

**정리**
- super() 키워드를 통해 자신에 대응하는 상위 클래스 생성자를 호출한다.
- 생성자가 상위 클래스 생성자에게 객체 생성을 위임하고 있다.
- super() 말고 this()를 사용해 default 값을 사용해 생성자를 위임 할 수 있다.

**4.2.3 인터페이스에 선언된 프로퍼티 구현**

- 인터페이스에 있는 프로퍼티 선언에는 뒷받침하는 필드나 게덜 정보가 들어있지 않다.
- 인터페이스는 아무 상태도 포함할 수 없으므로 상태를 저장할 필요가 있다면 인터페이스를 구현한 하위 클래스에서 상태 저장을 위한 프로퍼티 등을 만들어야 한다.

```kotlin
interface User {
    val nickname: String
}

class PrivateUser(override val nickname: String) : User

class SubscribeUser(val email: String): User {
    override val nickname: String
        get() = email.substringBefore('@') //커스텀 게터
}

class FacebookUser(val accountId: Int) : User {
    override val nickname = getFacebookName(accountId)
}
```
**커스텀 게터와 세터**

자바에서는 변수를 초기화 해줄 때 미리 검증단계를 아래와 같이 구현합니다.

```java
class Test {
  private String name;

  public void setName(String name) {
    if (TextUtils.ieEmpty(name) == false) {
      this.name = name;
    } else {
      this.name = "";
    }
  }

  public String getName() {
    return TextUtils.isEmpty(name) == false ? name : "";
  }
}
```

코틀린에서는 커스텀 게터와 세터를 사용해 별도의 메소드를 생성하지 않고 값의 검증과 초기화를 진행할 수 있다.

```kotlin
class Test {
var name: String = ""
  get() = if (field.length > 0) field else "name"
  set (value) {
      if (value.length > 0) field = value else ""
  }
}
```

**Backing Fields**

위에 코틀린 코드에서 사용한 field를 Backing Fields라고 합니다. Backing Fields를 사용해 검증후 값을 초기화 하거나 리턴할 수 있습니다.



### 4.3 컴파일러가 생성한 메소드: 데이터 클래스와 클래스 위임

**4.3.1 모든 클래스가 정의해야 하는 메소드**

자바 플랫폼에서는 클래스가 equals, hashCode, toString 등의 메소드를 구현해야 한다.

```kotlin
class Client(val name: String, val postalCode: Int) {
    override fun equals(other: Any?): Boolean {
        if(other == null || other !is Client)
            return false
        return name == other.name &&
                postalCode == other.postalCode
    }

    override fun toString() = "Client(name=$name, postalCode=$postalCode)"

    override fun hashCode() = name.hashCode() * 31 + postalCode

    fun copy(name: String = this.name,
             postalCode: Int = this.postalCode) = Client(name, postalCode)
}
```

- 문자열 표현 : toString

- 객체의 동등성 : equals()
서로 다른 두 객체가 내부에 동일한 데이터를 포함히는 경우 그 둘을 동등한 객체로 간주해야 할 수 있다.

**동등성 연산자 ==를 사용함**

자바에서는 ==를 원시 타입과 참조 타입을 비교할 때 사용한다. 원시 타입의 경우 ==는 두 피연산자의 값이 같은지 비교한다. 반면 참조 타입의 경우 ==는 두 피연산잔의 주소가 같은지를 비교한다.
따라서 자바에서는 두 객체의 동등성을 알려면 equals를 호출해야한다. 자바에서는 equals 대신 ==를 호출하면 문제가 될 수 있다.

코틀린에서는 == 연산자가 두 객체를 비교하는 기본 방법이다. ==는 내부적으로 equals를 호출해서 객체를 비교한다. 따라서 클래스가 equals를 오버라이드 하면 ==를 통해 안전하게 그 클래스의 인스턴스를 비교할 수 있다.
참조 비교를 위해서는 === 연산자를 사용할 수 있다. === 연산자는 자바에서 객체의 참조를 비교할 때 사용하는 == 연산자와 같다.

- 해시 컨테이너: hashCode()
자바에서는 equals 를 오버라이드할 때 반드시 hashCode 도 함께 오버라이드해야 한다.

JVM 언어에서는 hashCode가 지켜야 하는 "equals()가 true를 반환하는 두 객체는 반드시 같은 hashCode()를 반환해야 한다"라는 제약이 있다.

```kotlin
val processed = hashSetOf(Client("blackJin",0427))
processed.contains(Client("blackJin",0427))
// hashCode를 정의 하지 않았으면 false 값이 나온다.
```


**4.3.2 데이터 클래스 : 모든 클래스가 정의해야 하는 메소드 자동 생성**

```kotlin
data class Client(val name: String, val postalCode: Int)
```

- 인스턴스 간 비교를 위한 equals
- HashMap과 같은 해시 기반 컨테이너에서 키로 사용할 수 있는 hashCode
- 클래스의 각 필드를 선언 순서대로 표시하는 문자열 표현을 만들어 주는 toString

**equals와 hashCode는 주 생성자에 나열된 모든 프로퍼티를 고려해 만들어진다.**


**데이터 클래스와 불변성: copy() 메소드**

데이터 클래스의 모든 프로퍼티를 읽기 전용(val)으로 만들어서 데이터 클래스를 불변 클래스로 만들라고 권장한다.<br>
불변 객체를 사용하면 프로그램에 대해 훨씬 쉽게 추론할 수 있다. 특히 다중스레드 프로그램의 경우 이런 성질은 더 중요하다.<br>

객체를 복사하면서 일부 프로퍼티를 바꿀 수 있게 해주는 copy 메소드 객체를 메모리상에서 직접 바꾸는 대신 복사본을 만드는 편이 더 낫다.
복사본은 제거해도 프로그램에서 원본을 참조하는 다른 부분에 전혀 영향을 끼치지 않는다.

```kotlin
val lee = Client("이계영", 4122)
println(lee.copy(postalCode = 4000))
```

**4.3.3 클래스 위임: by 키워드 사용**

- 대규모 객체지향 시스템을 설계할 때 시스템을 취약하게 만드는 문제는 보통 구현 상속에 의해 발생한다. 하위 클래스가 상위 클래스의 메소드 중 일부를 오버라이드하면 하위 클래스는 상위 클래스의 세부 구현 사항에 의존하게 된다.
- 시스템이 변함에 따라 상위 클래스의 구현이 바뀌거나 상위 클래스에 새로운 메소드가 추가된다. 그 과정에서 하위 클래스가 상위 클래스에 대해 갖고 있던 가정이 깨져서 코드가 정상적으로 작동하지 못하는 경우가 생길 수 있다.
- 우리는 종종 상속을 허용하지 않는 클래스에 새로운 동작을 추가해야 할 때가 있다. 이럴 때 사용하는 일반적인 방법이 데코레이터패턴이다. 이 패턴의 핵심은 상속을 허용하지 않는 클래스 대신 사용할 수 있는 새로운 클래스를 만들되
기존 클래스와 같은 인터페이스를 데코레이터가 제공하게 만들고, 기존 클래스를 데코레이터 내부에 필드로 유지하는 것이다. 이때 새로 정의해야 하는 기능은 데코레이터의 메소드에 새로 정의하고 기존 기능이 그대로 필요한 부분은 데코레이터의 메소드가 기존 클래스의 메소드에게 요청을 전달한다.
- 하지만 이런 접근 방법의 단점은 준비 코드가 상당히 많이 필요하다는 점이다. 예를 들어 Collection 같이 비교적 단순한 인터페이스를 구현하면서 아무 동작도 변경하지 않는 데코레이터를 만들 때조차도 복잡한 코드를 작성해야 한다.

```kotlin
class DelegationCollection<T> : Collection<T> {
    private val innerList = arrayListOf<T>()

    override val size: Int get() = innerList.size
    override fun isEmpty(): Boolean = innerList.isEmpty()
    override fun contains(element: T): Boolean = innerList.contains(element)
    override fun iterator(): Iterator<T> = innerList.iterator()
    override fun containsAll(elements: Collection<T>) : Boolean =
        innerList.containsAll(elements)
}
```
이런 위임을 언어가 제공하는 일급 시민 기능으로 지원한다는 점이 코틀린의 장점이다. 인터페이스를 구현할 때 by 키워드를 통해 그 인터페이스에 대한 구현을 가른 객체에 위임 중이라는 사실을 명시할 수 있다. 다음은 앞의 예제를 위임을 사용해 재작성한 코드다.

```kotlin
class DelegationCollection<T>(
    innerList: Collection<T> = ArrayList<T>()
    ) : Collection<T> by innerList { }
```
클래스 안에 있던 모든 메소드 정의가 없어졌다. 컴파일러가 그런 전달 메소드를 자동으로 생성하며 자동 생성한 코드의 구현은 DelegationCollection에 있던 구현과 비슷하다.

메소드 중 일부의 동작을 변경하고 싶은 경우 메소드를 오버라이드하면 컴파일러가 생성한 메소드 대신 오버라이드한 메소드가 쓰인다. 기존 클래스의 메소드에 위임하는 기본 구현으로 충분한 메소드는 따로 오버라이드할 필요가 없다.
원소를 추가하려고 시고한 윗수를 기록하는 컬렉션을 구현해보자.

```kotlin
class CountingSet<T> (
    val innerSet: MutableCollection<T> = HashSet<T>()
) : MutableCollection<T> by innerSet { //MutableCollection의 구현을 innerSet에게 위임한다.

    var objectsAdded = 0

    // add, addAll 이 두 메소드는 위임하지 않고 새로운 구현을 제공한다.
    override fun add(element: T): Boolean {
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        objectsAdded += elements.size
        return innerSet.addAll(elements)
    }
}
```
add와 addAll을 오버라이드해서 카운터를 증가시키고, MutableCollection 인터페이스의 나머지 메소드는 내부 컨테이너(innerSet)에게 위임한다.
이때 CountingSet에 MutableCollection의 구현 방식에 대한 의존관계가 생기지 않는다는 점이 중요하다. 에를 들어 addAll을 처리할 때 루프를 돌면서
add를 호출할 수도 있지만, 최적화를 위해 다른 방식을 택할 수도 있다. 클라이언트 코드가 CountinSet의 코드를 호출할 때 발생하는 일은 CountingSet 안에서
마음대로 제어할 수 있지만, CountingSet 코드는 위임 대상 내부 클래스인 MutableCollection에 문서화된 API를 활용한다. 그러므로 내부 클래스 MutableCollection이
문서화된 API를 변경하지 않는 한 CountinSet 코드가 계속 잘 작동할 것임을 확신 할 수 있다.

### 4.4 object 키워드: 클래스 선언과 인스턴스 생성

클래스를 정의하면서 동시에 인스턴스(객체)를 생성한다.

**object 키워드는 다음과 같은 상황에 사용할 수 있다.**
- 객체 선언은 싱글턴을 정의하는 방법 중 하나다
- 동반 객체는 인스턴스 메소드는 아니지만 어떤 클래스와 관련 있는 메소드와 팩토리 메소드를 담을 때 쓰인다. 동반 객체 메소드에 접근할 때는 동반 객체가 포함된 클래스의 이름을 사용할 수 있다.
- 객체 식은 자바의 무명 내부 클래스 대신 쓰인다

**4.4.1 객체 선언: 싱글턴을 쉽게 만들기**
```kotlin
object Payroll {
    val allEmployees = arrayListOf<Person>()

    fun calculateSalary() {
        ...
    }
}
```



**4.4.2 동반 객체: 토리 메소드와 정적 맴버가 들어갈 장소**
```kotlin
class MyUser private constructor(val nickname: String) {
    companion object {
        fun newSubscribingUser(email: String) =
            MyUser(email.substringBefore("@"))
    }
}

fun main() {
    val subscribingUser = MyUser.newSubscribingUser("blackjin@tistory.com")
    println(subscribingUser.nickname)
}
```


**4.4.3 동반 객체를 일반 객체처럼 사용**

동반 객체는 클래스 안에 정의된 일반 객체다. 따라서 동반 객체에 이름을 붙이거나, 동반 객체가 인터페이스를 상속하거나, 공반 객체 안에 확장 함수와 프로퍼티를 정의할 수 있다.


**4.4.4 객체 식: 무명 내부 클래스를 다른 방식으로 작성**

무명 객체는 자바의 무명 내부 클래스를 대신한다.

- 객체 선언과 달리 무명 객체는 싱글턴이 아니다. 객체 식이 쓰일 때마다 새로운 인스턴스가 생성된다.


## 요약

- 코틀린의 인터페이스는 자바 인터페이스와 비슷하지만 디폴트 구현을 포함할 수 있고, 프로퍼티도 포함할 수 있다.
- 모든 코틀린 선언은 기본적으로 final이며 public이다.
- 선언이 final이 되지 않게 만들려면 앞에 open을 붙여야 한다.
- internal 선언은 같은 모듈 안에서만 볼 수 있다.
- 중첩 클래스는 기본적으로 내부 클래스가 아니다. 바깥쪽 클래스에 대한 참조를 중첩 클래스 안에 포함시키려면 inner 키워드를 중첩 클래스 선언 앞에 붙여서 내부 클래스로 만들어야 한다.
- sealed 클래스를 상속하는 클래스를 정의하려면 반드시 부모 클래스 정의 안에 중첩 클래스로 정의해야 한다.(코틀린 1.1 부터는 같은 파일 안에만 있으면 된다)
- 초기화 블록과 부 생성자를 활용해 클래스 인스턴스를 더 유연하게 초기화할 수 있다.
- field 식별자를 통해 프로퍼티 접근자 안에서 프로퍼티의 데이터를 저장하는 데 쓰이는 뒷받침하는 필드를 참조할 수 있다.
- 데이터 클래스를 사용하면 컴파일러가 equals, hashCode, toString, copy 등의 메소드를 자동으로 생성해준다.
- 클래스 위임을 사용하면 위임 패턴을 구현할 때 필요한 수많은 성가신 준비 코드를 줄일 수 있다.
- 객체 선언을 사용하면 코틀린 답게 싱글턴 클래스를 정의할 수 았다.
- 동반 객체는 자바의 정적 메소드와 필드 정의를 대신한다.
- 동반 객체도 다른 객체와 마찬가지로 인터페이스를 구현할 수 있다. 외부에서 동반 객체에 대한 확장 함수와 프로퍼티를 정의할 수 있다.
- 코틀린의 객체 식은 자바의 무명 내부 클래스를 대신한다. 하지만 코틀린 객체식은 여러 인스턴스를 구현하거나 객체가 포함된 영역에 있는 변수의 값을 변경할 수 있는 등 자바 무명 내부 클래스보다 더 많은 기능을 제공한다.