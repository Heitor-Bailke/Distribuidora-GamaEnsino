import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import DistribuidoraResolve from './route/distribuidora-routing-resolve.service';

const distribuidoraRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/distribuidora.component').then(m => m.DistribuidoraComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/distribuidora-detail.component').then(m => m.DistribuidoraDetailComponent),
    resolve: {
      distribuidora: DistribuidoraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/distribuidora-update.component').then(m => m.DistribuidoraUpdateComponent),
    resolve: {
      distribuidora: DistribuidoraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/distribuidora-update.component').then(m => m.DistribuidoraUpdateComponent),
    resolve: {
      distribuidora: DistribuidoraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default distribuidoraRoute;
