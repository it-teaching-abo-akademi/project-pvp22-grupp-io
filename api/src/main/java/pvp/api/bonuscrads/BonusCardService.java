package pvp.api.bonuscrads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pvp.models.interfaces.BonusCard;

import java.util.List;
import java.util.UUID;

@Service
public class BonusCardService {
    private final BonusCardAccessService bonusCardAccessService;

    @Autowired
    public BonusCardService(BonusCardAccessService bonusCardAccessService) {
        this.bonusCardAccessService = bonusCardAccessService;
    }

    List<BonusCard> getAllBonusCards() {
        return bonusCardAccessService.getAllBonusCards();
    }

    public BonusCard getBonusCardId(int id) {return bonusCardAccessService.getBonusCardId(id);}

    void addNewBonusCard(BonusCard card) {
        bonusCardAccessService.inserBonusCard(card);
    }
}
