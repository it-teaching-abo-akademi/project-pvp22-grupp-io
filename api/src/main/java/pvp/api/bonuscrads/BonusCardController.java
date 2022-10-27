package pvp.api.bonuscrads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pvp.models.interfaces.BonusCard;

import java.util.List;


@RestController
@RequestMapping("api/bonuscards")
public class BonusCardController {

    private final BonusCardService bonusCardService;

    @Autowired
    public BonusCardController(BonusCardService bonusCardService) {
        this.bonusCardService = bonusCardService;
    }

    @GetMapping
    public List<BonusCard> getAllBonusCards() {
        return this.bonusCardService.getAllBonusCards();
    }

    @GetMapping("/{id}")
    public BonusCard getBonusCardId(@PathVariable(value = "id") Integer id){
        return bonusCardService.getBonusCardId(id);
    }

    @PostMapping
    public void addNewBonusCard(@RequestBody BonusCard bonusCard) {
        this.bonusCardService.addNewBonusCard(bonusCard);
    }

}
