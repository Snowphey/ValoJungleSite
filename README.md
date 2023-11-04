
# ValoJungleSite

Site d'organisation d'évènements multijoueur Minecraft (LG UHC de Artutu).


## Installation

Pour faire fonctionner le site, il suffit de cloner l'archive, par exemple avec la commande Bash ```git clone```.

Pour le lancer, ouvrez le projet dans un IDE Java puis lancez le serveur web. Le site est désormais disponible à l'URL ```localhost:8080```.

## Utilisation

Par défaut, le site peut être parcouru en créant un compte de joueur via la page d'inscription, puis en utilisant la page de connexion pour accéder aux fonctionnalités de participation aux parties et de gestion de guildes.

Néanmoins, la base de données du site sera initialement vide, donc aucune partie ne sera disponible, et il est impossible pour un compte de joueur d'en créer une.

Pour créer des parties et des modes de jeu, il faudra d'abord créer un compte d'organisateur sur le site. Pour cela, plusieurs méthodes sont possibles. 


### Désactiver la sécurité

La méthode la plus simple étant de se rendre dans le fichier ```SecurityConfig.java``` où vous trouverez des instructions pour désactiver la sécurité du site. 

Vous pourrez alors accéder au panel administrateur présent à l'URL ```localhost:8080/admin```, depuis lequel vous pourrez vous créer un compte d'organisateur.

Pensez à réactiver la sécurité après avoir créé votre compte d'organisateur (toujours en suivant les instructions fournies dans ```SecurityConfig.java```).


### Insérer manuellement un nouvel organisateur

Éventuellement, pour créer un nouvel organisateur, vous pouvez également manipuler le contenu de la base de données en accédant au panel H2-console (accessible à l'URL ```localhost:8080/h2-console```). Pour vous y connecter, utilisez les informations présentes dans le fichier ```application.properties``` rappelées ici :

JDBC URL: jdbc:h2:file:./data/db \
User Name: sa \
Password: password

Ensuite, il faudra créer un tuple de la table "Utilisateur" ayant l'attribut "role" set à "admin".
Prenez d'ailleurs garde au hashage des mots de passe, le password saisi doit être la version hashée du mot de passe souhaité (algorithme BCrypt).

Puis ajouter un tuple de la table "Organisateur", où l'attribut "utilisateur_id" sera set à l'id de l'utilisateur que vous venez de créer.