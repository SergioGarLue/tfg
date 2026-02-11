# TODO: Improve Services with Business Logic

## CarritoService ✅
- Add addJuegoToCarrito method: Validate user exists, game exists, not already in cart, add game.
- Add removeJuegoFromCarrito method: Validate game in cart, remove it.
- Add getTotalPrice method: Calculate total price of games in cart.
- Add checkout method: Validate cart not empty, create Compra, clear cart, update user library.

## UsuarioService ✅
- Enhance validaPasswd with real validation (length, complexity).
- Add login method with authentication logic.
- Add updateProfile method with validations.

## AmigoService
- Add sendFriendRequest: Validate users exist, not already friends, create request.
- Add acceptFriendRequest: Validate request exists, pending, accept it.
- Add rejectFriendRequest: Validate and reject.

## JuegoService
- Add validateAndSave: Validate game data (title not empty, price positive), save.
- Enhance search methods with additional filters if needed.

## CompraService
- Add processPurchase: Validate payment, create Compra, update user library, clear cart.

## Other Services
- Review and add logic to ColeccionService, ListaDeseadosService, etc., similarly.
