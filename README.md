# GankMeDiddy

***GankMeDiddy*** es una plataforma web  diseñada para la compra de videojuegos online, gestión de colecciones personales y conexión social entre jugadores. Este proyecto busca unificar la experiencia de compra con la de una comunidad activa.

## Instalacion


## Funcionalidades
- **Colección :** Biblioteca para que cada usuario pueda ver sus juegos.

- **Amistades :** Apartado con amistades donde poder ver los amigos conectados.

- **Notificación :** Notifica a los usuarios la compra de cada videojuego y cuando se conecta un amigo.

- **Reseñas :** Valoracion que pueden hacer los usuarios de cada juego dandole una puntuación.

- **Carrito :** Almacena juegos que el usuario añada para que posteriormente pueda realizar la compra de esos juegos.

- **Metodo de pago :** Multiples metodos de pago distintos para que el usuario decida.
    - PayPal
    - Debit Card
    - Credit Card

- **Whislist :** Lista de deseados que almacenara los juegos que el usuario desee.
- **Desarrollador/Editor :** La página de editores/desarrolladores mostrando sus juegos y DLCs.

## Tecnologias Utilizadas
- **Java**
    - SpringBoot
- **HTML5**
- **CSS3**
    - FontAwesome
- **JavaScript**


@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(nullable = false)
    private Integer score; // Aquí guardas el 1, 2, 3, 4 o 5

    // Opcional: para que un usuario no vote dos veces el mismo juego
    // @Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "game_id"})})
}