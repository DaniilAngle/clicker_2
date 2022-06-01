package clicker.tests

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import clicker._
import clicker.database.DatabaseActor
import clicker.model.GameActor
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.duration._

class TestClicks extends TestKit(ActorSystem("TestClicks"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }


  "A Clicker Game" must {
    "react to user clicks with shovels appropriately" in {

      val database = system.actorOf(Props(classOf[DatabaseActor], "test"))
      val gameActor = system.actorOf(Props(classOf[GameActor], "username", database))

      var i: Int = 0
      for (i <- 1 to 9 ) {
        gameActor ! ClickGold
      }


      gameActor ! BuyEquipment("shovel")
      // Nothing should happen
      gameActor ! ClickGold
      // Can buy shovel now
      gameActor ! BuyEquipment("shovel")
      // 2 gold per click
      gameActor ! ClickGold
      expectNoMessage(50.millis)
      gameActor ! Update
      val gs: GameState = expectMsgType[GameState](1000.millis)

      val gameStateJSON: String = gs.gameState

      val gameState: JsValue = Json.parse(gameStateJSON)
      println(gameState)
      val gold: Int = (gameState \ "gold").as[Int]

      assert(gold === 2)
    }
  }
}
