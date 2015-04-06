package first

//import scala.actors.Actor
import akka.actor._
import akka.actor.ActorSystem
import akka.actor.Props


case class Addition(data:Array[Int], s:Int, e:Int)
case class res(x:Int, y:Int)

class HelloActor(r:ActorRef) extends Actor {

def receive = {
case Addition(data, s, e) => {
if(e - s < 4){
var temp = 0;
for(i <- s to e)
temp += data(i)
r ! res(temp, e)
}
else{
var child1 = context.actorOf(Props(classOf[HelloActor], r))
var child2 = context.actorOf(Props(classOf[HelloActor], r))
child1 ! Addition(data, s,(s+e)/2)
child2 ! Addition(data, (s+e)/2 + 1, e)

}
}
case _       => println("huh?")
}
}



class Result(z:Int) extends Actor{
var state  = 0;
def receive = {
case res(x,y) => {
state+=x;
if(y == z){
println("The result:"+state)
}
}
}

}


object Main{
 def main(args:Array[String]){
val system = ActorSystem("AddSystem")
val listener = system.actorOf(Props(classOf[Result], 10));
var arr = Array[Int](1,2,3, 4,5,6,7,8,9,10)
val add = system.actorOf(Props(classOf[HelloActor],listener))
add ! Addition(arr, 0, 9)
}
}