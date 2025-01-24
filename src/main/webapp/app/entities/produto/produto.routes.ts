import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import ProdutoResolve from './route/produto-routing-resolve.service';

const produtoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/produto.component').then(m => m.ProdutoComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/produto-detail.component').then(m => m.ProdutoDetailComponent),
    resolve: {
      produto: ProdutoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/produto-update.component').then(m => m.ProdutoUpdateComponent),
    resolve: {
      produto: ProdutoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/produto-update.component').then(m => m.ProdutoUpdateComponent),
    resolve: {
      produto: ProdutoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default produtoRoute;
