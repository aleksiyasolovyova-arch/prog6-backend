package kdg.be.prog6.kdg.restaurant.ports.in;

import kdg.be.prog6.kdg.restaurant.adapters.in.response.PendingChangesResponse;

public interface GetPendingChangesPort {
    PendingChangesResponse getPendingChanges(GetPendingChangesQuery query);
}
