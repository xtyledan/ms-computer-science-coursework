import random
import sys

try:
    import pygame
except ModuleNotFoundError:
    print("This game needs Pygame installed. Try: C:/Users/xtyledan/.local/bin/python3.14.exe -m pip install pygame")
    raise SystemExit(1)


# -----------------------------
# Game setup and constants
# -----------------------------
WIDTH = 800
HEIGHT = 600
HUD_HEIGHT = 80
PLAY_AREA_TOP = HUD_HEIGHT
FPS = 60

WHITE = (245, 245, 245)
BLACK = (20, 20, 20)
BLUE = (60, 130, 255)
GREEN = (70, 210, 110)
RED = (235, 70, 70)
YELLOW = (240, 210, 70)
GRAY = (70, 70, 70)
BG_TOP = (24, 32, 48)
BG_BOTTOM = (14, 18, 28)

PLAYER_SIZE = 40
PLAYER_SPEED = 5
COLLECTIBLE_RADIUS = 14
ENEMY_RADIUS = 18
WIN_SCORE = 10


def clamp(value, minimum, maximum):
    return max(minimum, min(value, maximum))


def circle_rect_collision(circle_x, circle_y, radius, rect):
    """Check whether a circle and rectangle overlap."""
    closest_x = clamp(circle_x, rect.left, rect.right)
    closest_y = clamp(circle_y, rect.top, rect.bottom)
    dx = circle_x - closest_x
    dy = circle_y - closest_y
    return dx * dx + dy * dy <= radius * radius


def draw_vertical_gradient(surface, top_color, bottom_color):
    """Draw a simple background gradient using lines."""
    for y in range(surface.get_height()):
        blend = y / max(1, surface.get_height() - 1)
        color = (
            int(top_color[0] + (bottom_color[0] - top_color[0]) * blend),
            int(top_color[1] + (bottom_color[1] - top_color[1]) * blend),
            int(top_color[2] + (bottom_color[2] - top_color[2]) * blend),
        )
        pygame.draw.line(surface, color, (0, y), (surface.get_width(), y))


def make_enemy(screen_rect):
    """Create one bouncing enemy with a random direction and speed."""
    spawn_x = random.randint(PLAY_AREA_TOP + ENEMY_RADIUS, screen_rect.width - ENEMY_RADIUS)
    spawn_y = random.randint(PLAY_AREA_TOP + ENEMY_RADIUS, screen_rect.height - ENEMY_RADIUS)

    direction_x = random.choice([-1, 1])
    direction_y = random.choice([-1, 1])
    speed = random.randint(2, 4)

    return {
        "x": float(spawn_x),
        "y": float(spawn_y),
        "vx": direction_x * speed,
        "vy": direction_y * speed,
        "radius": ENEMY_RADIUS,
    }


def spawn_collectible(player_rect, enemies, screen_rect):
    """Place the collectible somewhere safe and away from the player and enemies."""
    for _ in range(500):
        x = random.randint(COLLECTIBLE_RADIUS + 20, screen_rect.width - COLLECTIBLE_RADIUS - 20)
        y = random.randint(PLAY_AREA_TOP + COLLECTIBLE_RADIUS + 10, screen_rect.height - COLLECTIBLE_RADIUS - 20)

        collectible_rect = pygame.Rect(
            x - COLLECTIBLE_RADIUS,
            y - COLLECTIBLE_RADIUS,
            COLLECTIBLE_RADIUS * 2,
            COLLECTIBLE_RADIUS * 2,
        )

        if collectible_rect.colliderect(player_rect.inflate(40, 40)):
            continue

        too_close = False
        for enemy in enemies:
            dx = x - enemy["x"]
            dy = y - enemy["y"]
            min_distance = COLLECTIBLE_RADIUS + enemy["radius"] + 30
            if dx * dx + dy * dy < min_distance * min_distance:
                too_close = True
                break

        if not too_close:
            return [x, y]

    # Fallback if the loop somehow cannot find a perfect spot.
    return [screen_rect.centerx, screen_rect.centery]


def reset_game(screen_rect):
    """Reset every gameplay variable to a fresh state."""
    player = pygame.Rect(0, 0, PLAYER_SIZE, PLAYER_SIZE)
    player.center = (screen_rect.centerx, screen_rect.bottom - 80)

    enemies = [make_enemy(screen_rect), make_enemy(screen_rect), make_enemy(screen_rect)]
    collectible = spawn_collectible(player, enemies, screen_rect)

    return {
        "player": player,
        "enemies": enemies,
        "collectible": collectible,
        "score": 0,
        "state": "playing",
        "enemy_speed_bonus": 0,
    }


def main():
    pygame.init()
    pygame.display.set_caption("Dodge and Collect")

    screen = pygame.display.set_mode((WIDTH, HEIGHT))
    screen_rect = screen.get_rect()
    clock = pygame.time.Clock()
    font = pygame.font.SysFont("arial", 24)
    big_font = pygame.font.SysFont("arial", 48, bold=True)

    game = reset_game(screen_rect)

    running = True
    while running:
        dt = clock.tick(FPS)

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_ESCAPE:
                    running = False
                elif event.key == pygame.K_r and game["state"] != "playing":
                    game = reset_game(screen_rect)

        keys = pygame.key.get_pressed()

        # Player movement only happens while the game is active.
        if game["state"] == "playing":
            move_x = 0
            move_y = 0

            if keys[pygame.K_LEFT] or keys[pygame.K_a]:
                move_x -= PLAYER_SPEED
            if keys[pygame.K_RIGHT] or keys[pygame.K_d]:
                move_x += PLAYER_SPEED
            if keys[pygame.K_UP] or keys[pygame.K_w]:
                move_y -= PLAYER_SPEED
            if keys[pygame.K_DOWN] or keys[pygame.K_s]:
                move_y += PLAYER_SPEED

            game["player"].x += move_x
            game["player"].y += move_y

            # Keep the player inside the play area.
            game["player"].left = clamp(game["player"].left, 0, WIDTH - PLAYER_SIZE)
            game["player"].top = clamp(game["player"].top, PLAY_AREA_TOP, HEIGHT - PLAYER_SIZE)

            # Collect the green circle for points.
            collectible_x, collectible_y = game["collectible"]
            player_center_x = game["player"].centerx
            player_center_y = game["player"].centery
            dx = player_center_x - collectible_x
            dy = player_center_y - collectible_y
            collection_distance = (PLAYER_SIZE // 2) + COLLECTIBLE_RADIUS

            if dx * dx + dy * dy <= collection_distance * collection_distance:
                game["score"] += 1

                # Small difficulty ramp: enemies speed up as the score rises.
                if game["score"] % 3 == 0:
                    game["enemy_speed_bonus"] += 1

                if game["score"] >= WIN_SCORE:
                    game["state"] = "won"
                else:
                    game["collectible"] = spawn_collectible(game["player"], game["enemies"], screen_rect)

            # Move enemies and bounce them off the walls.
            for enemy in game["enemies"]:
                enemy["x"] += enemy["vx"] + (1 if enemy["vx"] > 0 else -1) * 0.1 * game["enemy_speed_bonus"]
                enemy["y"] += enemy["vy"] + (1 if enemy["vy"] > 0 else -1) * 0.1 * game["enemy_speed_bonus"]

                if enemy["x"] - enemy["radius"] <= 0:
                    enemy["x"] = enemy["radius"]
                    enemy["vx"] *= -1
                elif enemy["x"] + enemy["radius"] >= WIDTH:
                    enemy["x"] = WIDTH - enemy["radius"]
                    enemy["vx"] *= -1

                if enemy["y"] - enemy["radius"] <= PLAY_AREA_TOP:
                    enemy["y"] = PLAY_AREA_TOP + enemy["radius"]
                    enemy["vy"] *= -1
                elif enemy["y"] + enemy["radius"] >= HEIGHT:
                    enemy["y"] = HEIGHT - enemy["radius"]
                    enemy["vy"] *= -1

                if circle_rect_collision(enemy["x"], enemy["y"], enemy["radius"], game["player"]):
                    game["state"] = "lost"

        # -----------------------------
        # Drawing
        # -----------------------------
        draw_vertical_gradient(screen, BG_TOP, BG_BOTTOM)

        # HUD bar at the top.
        pygame.draw.rect(screen, (16, 20, 32), (0, 0, WIDTH, HUD_HEIGHT))
        pygame.draw.line(screen, GRAY, (0, HUD_HEIGHT), (WIDTH, HUD_HEIGHT), 2)

        score_text = font.render(f"Score: {game['score']} / {WIN_SCORE}", True, WHITE)
        instructions_text = font.render("Move: Arrow Keys or WASD | Collect green circles | Avoid red enemies | R to restart", True, WHITE)
        screen.blit(score_text, (20, 16))
        screen.blit(instructions_text, (20, 44))

        # Draw the collectible and enemies.
        pygame.draw.circle(screen, GREEN, game["collectible"], COLLECTIBLE_RADIUS)

        for enemy in game["enemies"]:
            pygame.draw.circle(screen, RED, (int(enemy["x"]), int(enemy["y"])), enemy["radius"])
            pygame.draw.circle(screen, (255, 160, 160), (int(enemy["x"]), int(enemy["y"])), enemy["radius"], 2)

        # Draw the player as a blue square.
        pygame.draw.rect(screen, BLUE, game["player"])
        pygame.draw.rect(screen, WHITE, game["player"], 2)

        # Center message for win/loss states.
        if game["state"] in {"won", "lost"}:
            overlay = pygame.Surface((WIDTH, HEIGHT), pygame.SRCALPHA)
            overlay.fill((0, 0, 0, 120))
            screen.blit(overlay, (0, 0))

            if game["state"] == "won":
                title = "You Win!"
                message = "You reached 10 points. Press R to play again."
                title_color = YELLOW
            else:
                title = "Game Over"
                message = "An enemy hit you. Press R to try again."
                title_color = RED

            title_surface = big_font.render(title, True, title_color)
            message_surface = font.render(message, True, WHITE)

            title_rect = title_surface.get_rect(center=(WIDTH // 2, HEIGHT // 2 - 20))
            message_rect = message_surface.get_rect(center=(WIDTH // 2, HEIGHT // 2 + 26))
            screen.blit(title_surface, title_rect)
            screen.blit(message_surface, message_rect)

        pygame.display.flip()

    pygame.quit()
    sys.exit()


if __name__ == "__main__":
    main()