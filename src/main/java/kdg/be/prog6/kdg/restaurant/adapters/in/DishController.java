package kdg.be.prog6.kdg.restaurant.adapters.in;

import kdg.be.prog6.kdg.restaurant.adapters.in.request.CreateDishDraftRequest;
import kdg.be.prog6.kdg.restaurant.adapters.in.response.DishIdResponse;
import kdg.be.prog6.kdg.restaurant.adapters.in.response.DraftIdResponse;
import kdg.be.prog6.kdg.restaurant.domain.DishId;
import kdg.be.prog6.kdg.restaurant.domain.DraftId;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;
import kdg.be.prog6.kdg.restaurant.ports.in.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    private final CreateDishDraftPort dishDraftPort;
    private final PublishDishPort publishDishPort;
    private final UnpublishDishPort unpublishDishPort;

    public DishController(CreateDishDraftPort dishDraftPort, PublishDishPort publishDishPort, UnpublishDishPort unpublishDishPort) {
        this.dishDraftPort = dishDraftPort;
        this.publishDishPort = publishDishPort;
        this.unpublishDishPort = unpublishDishPort;
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

   @PostMapping("/drafts/{draftId}/publish")
   public ResponseEntity<DishIdResponse> publishDraft(
           @PathVariable UUID draftId,
           @RequestParam UUID restaurantId
   ) {
       DishId dishId = publishDishPort.publishDraft(
               new PublishDishDraftCommand(
                       RestaurantId.from(restaurantId),
                       DraftId.from(draftId)
               )
       );
       return ResponseEntity.ok(new DishIdResponse(dishId.uuid()));
   }

   @DeleteMapping("/{dishId}/unpublish")
    public ResponseEntity<Void> unpublishDish(
            @PathVariable UUID dishId,
            @RequestParam UUID restaurantId
   ) {
        unpublishDishPort.unpublishDraft(new UnpublishDishCommand(
                RestaurantId.from(restaurantId),
                DishId.from(dishId)));
        return ResponseEntity.noContent().build();
   }

}
