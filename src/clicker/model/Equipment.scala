package clicker.model

class Equipment {
  var shovelName: String = "Shovel"
  var excavatorName: String = "Excavator"
  var mineName: String = "Gold Mine"
  var shovelID: String = "shovel"
  var excavatorID: String = "excavator"
  var mineID: String = "mine"
  var shovelCost: Double = 10
  var excavatorCost: Double = 200
  var mineCost: Double = 1000
  var goldChange: Double = 0
  var shovel: Int = 0
  var excavator: Int = 0
  var mine: Int = 0
  var shovelCostChange: Double = 1.05
  var excavatorCostChange: Double = 1.1
  var mineCostChange: Double = 1.1
  var shovelPassive: Double = 0
  var excavatorPassive: Double = 10
  var minePassive: Double = 100

  def buyEquip(gold: Double, equipmentId: String): Unit ={
    if (equipmentId == shovelID && gold >= shovelCost) {
      goldChange -= shovelCost
      shovel += 1
      shovelCost *= shovelCostChange
    } else if (equipmentId == excavatorID && gold >= excavatorCost) {
      goldChange -= excavatorCost
      excavator += 1
      excavatorCost *= excavatorCostChange
    } else if (equipmentId == mineID && gold >= mineCost) {
      goldChange -= mineCost
      mine += 1
      mineCost *= mineCostChange
    }
  }
}
