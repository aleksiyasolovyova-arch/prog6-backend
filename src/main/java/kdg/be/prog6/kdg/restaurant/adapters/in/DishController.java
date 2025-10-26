package kdg.be.prog6.kdg.restaurant.adapters.in;

import kdg.be.prog6.kdg.restaurant.adapters.in.request.CreateDishDraftRequest;
import kdg.be.prog6.kdg.restaurant.adapters.in.response.DraftIdResponse;
import kdg.be.prog6.kdg.restaurant.domain.DraftId;
import kdg.be.prog6.kdg.restaurant.ports.in.CreateDishDraftPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    private final CreateDishDraftPort dishDraftPort;

    public DishController(CreateDishDraftPort dishDraftPort) {
        this.dishDraftPort = dishDraftPort;
    }

    @PostMapping("/drafts")
    public ResponseEntity<DraftIdResponse> createDishDraft(
            @RequestBody CreateDishDraftRequest request
    ) {
        DraftId id = dishDraftPort.createDishDraft(request.toCommand());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new DraftIdResponse(id.uuid()));
    }
}
