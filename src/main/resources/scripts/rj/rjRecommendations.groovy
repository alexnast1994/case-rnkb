import com.prime.db.rnkb.model.commucation.judgment.RecommendationConstruction
import com.prime.db.rnkb.model.commucation.judgment.RjWorksheet
import com.prime.db.rnkb.model.commucation.judgment.WorksheetRecommendations

List<RecommendationConstruction> recommendationConstructions = execution.getVariable("recommendationConstructionOut") as List<RecommendationConstruction>
RjWorksheet rjWorksheet = execution.getVariable("rjWorksheetOut") as RjWorksheet

List<WorksheetRecommendations> rjRecommendations = new ArrayList<>()
recommendationConstructions.each {rc ->
    WorksheetRecommendations recommendation = new WorksheetRecommendations()
    recommendation.recommendationId = rc
    recommendation.order = rc.id
    recommendation.worksheetId = rjWorksheet
    recommendation.isSelected = false
    rjRecommendations.add(recommendation)
}

execution.setVariable("rjRecommendations", rjRecommendations)