package clicker.tests

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import clicker._
import clicker.database.DatabaseActor
import clicker.model.GameActor
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.duration._

class TestIdle extends TestKit(ActorSystem("TestIdle"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }


  "A Clicker Game" must {
    "update passive excavator income appropriately" in {

      val database = system.actorOf(Props(classOf[DatabaseActor], "test"))
      val gameActor = system.actorOf(Props(classOf[GameActor], "excavate", database))

      var i: Int = 0
      for (i <- 1 to 200 ) {
        gameActor ! ClickGold
      }

      gameActor ! BuyEquipment("excavator")

      expectNoMessage(2000.millis)

      gameActor ! Update

      val gs: GameState = expectMsgType[GameState](1000.millis)

      val gameStateJSON: String = gs.gameState

      val gameState: JsValue = Json.parse(gameStateJSON)
      println(gameState)
      val gold: Double = (gameState \ "gold").as[Double]

      assert(gold >= 20 && gold < 30)
    }
  }

  "A Clicker Game" must {
    "update passive gold mine appropriately" in {

      val database = system.actorOf(Props(classOf[DatabaseActor], "test"))
      val gameActor = system.actorOf(Props(classOf[GameActor], "mine", database))

      var i: Int = 0
      for (i <- 1 to 1000 ) {
        gameActor ! ClickGold
      }

      gameActor ! BuyEquipment("mine")

      expectNoMessage(2000.millis)

      gameActor ! Update

      val gs: GameState = expectMsgType[GameState](1000.millis)

      val gameStateJSON: String = gs.gameState

      val gameState: JsValue = Json.parse(gameStateJSON)
      println(gameState)
      val gold: Double = (gameState \ "gold").as[Double]

      assert(gold >= 200 && gold < 300)
    }
  }
}
