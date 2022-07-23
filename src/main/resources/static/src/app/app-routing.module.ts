import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainLayoutComponent} from "./components/shared/main-layout/main-layout.component";
import {MainPageComponent} from "./components/main-page/main-page.component";
import {ProductPageComponent} from "./components/product-page/product-page.component";
import {CartPageComponent} from "./components/cart-page/cart-page.component";

const routes: Routes = [
  {path: '', component: MainLayoutComponent, children: [
      {path: '', redirectTo: '/', pathMatch: 'full'},
      {path: '', component: MainPageComponent},
      {path: 'product/:id', component: ProductPageComponent},
      {path: 'cart', component: CartPageComponent}
    ]
  },
  //{path: 'admin', loadChildren: './admin/admin.module#AdminModule'}
  {path: 'admin', loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule)}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
