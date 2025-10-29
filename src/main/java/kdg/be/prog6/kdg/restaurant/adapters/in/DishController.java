package kdg.be.prog6.kdg.restaurant.adapters.in;

import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.adapters.in.request.CreateDishDraftRequest;
import kdg.be.prog6.kdg.restaurant.adapters.in.request.SchedulePublishAllDraftsRequest;
import kdg.be.prog6.kdg.restaurant.adapters.in.request.UpdateDishDraftRequest;
import kdg.be.prog6.kdg.restaurant.adapters.in.response.*;
import kdg.be.prog6.kdg.restaurant.core.ViewDishDetailsUseCaseImpl;
import kdg.be.prog6.kdg.restaurant.domain.DishId;
import kdg.be.prog6.kdg.restaurant.domain.DraftId;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.DishLimitExceededException;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.InvalidScheduleException;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.NoDraftsToPublishException;
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
    private final MarkOutOfStockPort markOutOfStockPort;
    private final MarkInStockPort markInStockPort;
    private final CreateDraftForEditingPort createDraftForEditingPort;
    private final EditDishDraftPort editDishDraftPort;
    private final DiscardDraftPort discardDraftPort;
    private final PublishAllDraftsPort publishAllDraftsPort;
    private final GetPendingChangesPort getPendingChangesPort;
    private final ViewDishDetailsUseCaseImpl viewDishDetailsUseCase;
    private final SchedulePublishAllDraftsPort schedulePublishAllDraftsPort;

    public DishController(CreateDishDraftPort dishDraftPort, PublishDishPort publishDishPort, UnpublishDishPort unpublishDishPort, MarkOutOfStockPort markOutOfStockPort, MarkInStockPort markInStockPort, CreateDraftForEditingPort createDraftForEditingPort, EditDishDraftPort editDishDraftPort, DiscardDraftPort discardDraftPort, PublishAllDraftsPort publishAllDraftsPort, GetPendingChangesPort getPendingChangesPort, ViewDishDetailsUseCaseImpl viewDishDetailsUseCase, SchedulePublishAllDraftsPort schedulePublishAllDraftsPort) {
        this.dishDraftPort = dishDraftPort;
        this.publishDishPort = publishDishPort;
        this.unpublishDishPort = unpublishDishPort;
        this.markOutOfStockPort = markOutOfStockPort;
        this.markInStockPort = markInStockPort;
        this.createDraftForEditingPort = createDraftForEditingPort;
        this.editDishDraftPort = editDishDraftPort;
        this.discardDraftPort = discardDraftPort;
        this.publishAllDraftsPort = publishAllDraftsPort;
        this.getPendingChangesPort = getPendingChangesPort;
        this.viewDishDetailsUseCase = viewDishDetailsUseCase;
        this.schedulePublishAllDraftsPort = schedulePublishAllDraftsPort;
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
    
    @PostMapping("/{dishId}/drafts")
    public ResponseEntity<DraftIdResponse> createDraftForEditing(
            @PathVariable UUID dishId,
            @RequestParam UUID restaurantId
    ) {
        DraftId draftId = createDraftForEditingPort.createDraftForEditing(
                new CreateDraftForEditingCommand(
                        DishId.from(dishId),
                        RestaurantId.from(restaurantId)
                )
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(new DraftIdResponse(draftId.uuid()));
    }

    @PatchMapping("/drafts/{draftId}")
    public ResponseEntity<Void> editDraft(
            @PathVariable UUID draftId,
            @RequestBody UpdateDishDraftRequest request
    ) {
       editDishDraftPort.editDraft(new EditDishDraftCommand(
               DraftId.from(draftId),
               request.restaurantId(),
               request.toDetails()
       ));
       return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/drafts/{draftId}")
    public ResponseEntity<Void> discardDraft(
            @PathVariable UUID draftId,
            @RequestParam UUID restaurantId
    ) {
        discardDraftPort.discardDraft(new DiscardDishDraftCommand(
                RestaurantId.from(restaurantId),
                DraftId.from(draftId)
        ));
        return ResponseEntity.noContent().build();
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

   @PostMapping("/publish-all")
   public ResponseEntity<PublishAllResponse> publishAllDrafts(
        @RequestParam UUID restaurantId
   ) {
        int publishedCount = publishAllDraftsPort.publishAllDrafts(
                new PublishAllDraftsCommand(RestaurantId.from(restaurantId))
                );
        return ResponseEntity.ok(new PublishAllResponse(publishedCount));
   }

   @GetMapping("/pending-changes")
   public ResponseEntity<PendingChangesResponse> getPendingChanges(
           @RequestParam UUID restaurantId
   ) {
        PendingChangesResponse response = getPendingChangesPort.getPendingChanges(
                new GetPendingChangesQuery(RestaurantId.from(restaurantId))
        );
        return ResponseEntity.ok(response);
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

   @PostMapping("/{dishId}/out-of-stock")
    public ResponseEntity<Void> markOutOfStock (
            @PathVariable UUID dishId,
            @RequestParam UUID restaurantId
   ) {
        markOutOfStockPort.markOutOfStock(new MarkOutOfStockCommand(
                RestaurantId.from(restaurantId),
                DishId.from(dishId)
        ));return ResponseEntity.noContent().build();

   }

   @PostMapping("/{dishId}/in-stock")
    public ResponseEntity<Void> markInStock (
            @PathVariable UUID dishId,
            @RequestParam UUID restaurantId
   ){
        markInStockPort.markInStock(new MarkInStockCommand(
                RestaurantId.from(restaurantId),
                DishId.from(dishId)
        ));return ResponseEntity.noContent().build();
   }

    @GetMapping("/{dishId}/details")
    public ResponseEntity<DishDetailsResponse> getDishDetails(
            @PathVariable UUID dishId,
            @RequestParam UUID restaurantId
    ) {
        DishDetailsResponse response = viewDishDetailsUseCase.getDishDetails(
                RestaurantId.from(restaurantId),
                DishId.from(dishId)
        );
        return ResponseEntity.ok(response);
    }


    @PostMapping("/{restaurantId}/dishes/schedule-publish-all")
    public ResponseEntity<Void> schedulePublishAllDrafts(
            @PathVariable UUID restaurantId,
            @RequestBody SchedulePublishAllDraftsRequest request) {

        try {
            SchedulePublishAllDraftsCommand command = new SchedulePublishAllDraftsCommand(
                    restaurantId,
                    request.publishAt()
            );

            schedulePublishAllDraftsPort.schedulePublishAllDrafts(command);

            return ResponseEntity.ok().build();

        } catch (InvalidScheduleException | NoDraftsToPublishException | DishLimitExceededException e) {
            return ResponseEntity.badRequest().build();
        } catch (RestaurantNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
