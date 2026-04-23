import sys
import requests
import json
import time
import re
import os

# Forzar salida UTF-8 en Windows para evitar errores con caracteres Unicode
if hasattr(sys.stdout, 'reconfigure'):
    sys.stdout.reconfigure(encoding='utf-8', errors='replace')

# --- CONFIGURACIÓN ---
LIMITE_JUEGOS = 1000
ARCHIVO_SALIDA = 'steam_top_1000_sellers.json'
PAUSA_ENTRE_PETICIONES = 1.5 
GUARDAR_CADA = 10 

def extraer_gb(texto_html):
    if not texto_html: return 0
    match = re.search(r'(\d+)\s*(GB|MB|Gb|Mb)', texto_html, re.IGNORECASE)
    if match:
        valor = float(match.group(1))
        unidad = match.group(2).upper()
        return valor if unidad == "GB" else round(valor / 1024, 2)
    return 0

def get_top_sellers_ids(total_games):
    ids = []
    print(f"🔎 Obteniendo lista de los {total_games} más vendidos...")
    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/120.0.0.0 Safari/537.36'}
    
    for start in range(0, total_games, 50):
        url = f"https://store.steampowered.com/search/results/?query&start={start}&count=50&filter=topsellers&supportedlang=spanish&ignore_preferences=1"
        
        try:
            res = requests.get(url, headers=headers, timeout=10)
            encontrados = re.findall(r'data-ds-appid="(\d+)"', res.text)
            
            if not encontrados:
                print(f"⚠️ No se han encontrado más IDs en el índice {start}.")
                break

            for _id in encontrados:
                if _id not in ids and len(ids) < total_games:
                    ids.append(_id)
            
            print(f"   > IDs acumulados: {len(ids)}/{total_games}")
            time.sleep(2) 
        except Exception as e:
            print(f"❌ Error en búsqueda: {e}")
            break
            
    return ids

def ejecutar_scrapper():
    if os.path.exists(ARCHIVO_SALIDA):
        with open(ARCHIVO_SALIDA, 'r', encoding='utf-8') as f:
            try:
                biblioteca = json.load(f)
            except:
                biblioteca = []
    else:
        biblioteca = []

    ids_procesados = {str(j['appid']) for j in biblioteca}
    todos_los_ids = get_top_sellers_ids(LIMITE_JUEGOS)
    ids_pendientes = [id for id in todos_los_ids if id not in ids_procesados]
    
    print(f"\n📋 Pendientes: {len(ids_pendientes)}\n")

    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/120.0.0.0'}

    for i, appid in enumerate(ids_pendientes, 1):
        detail_url = f"https://store.steampowered.com/api/appdetails?appids={appid}&l=spanish"
        
        try:
            res = requests.get(detail_url, headers=headers, timeout=10)
            
            if res.status_code == 429:
                print("🚨 Bloqueo temporal (429). Esperando 60 segundos...")
                time.sleep(60)
                continue 

            data_json = res.json()
            
            if data_json and data_json.get(str(appid), {}).get('success'):
                d = data_json[str(appid)]['data']
                
                req_min = d.get('pc_requirements', {}).get('minimum', '')
                
                # --- LIMITACIÓN A 20 CAPTURAS ---
                # Usamos [:20] al final para tomar solo los primeros 20 elementos
                lista_screenshots = [s['path_full'] for s in d.get('screenshots', [])][:20]

                info_precio = d.get('price_overview')
                if info_precio:
                    final_price = info_precio.get('final', 0) / 100
                    discount = info_precio.get('discount_percent', 0)
                else:
                    final_price = 0 if d.get('is_free') else "N/A"
                    discount = 0

                game_obj = {
                    "appid": int(appid),
                    "name": d.get('name'),
                    "type": d.get('type'),
                    "developer": ", ".join(d.get('developers', [])),
                    "release_date": d.get('release_date', {}).get('date'),
                    "genres": [g['description'] for g in d.get('genres', [])],
                    "description": d.get('short_description'),
                    "header_image": d.get('header_image'),
                    "screenshots": lista_screenshots,
                    "storage_gb": extraer_gb(req_min),
                    "price": {
                        "final": final_price,
                        "discount_percent": discount
                    },
                    "platforms": d.get('platforms')
                }
                
                biblioteca.append(game_obj)
                print(f"[{len(biblioteca)}] ✅ {game_obj['name']} ({len(lista_screenshots)} capturas)")

            if i % GUARDAR_CADA == 0:
                with open(ARCHIVO_SALIDA, 'w', encoding='utf-8') as f:
                    json.dump(biblioteca, f, ensure_ascii=False, indent=4)
                print(f"💾 Progreso guardado.")

            time.sleep(PAUSA_ENTRE_PETICIONES)

        except Exception as e:
            print(f"❌ Error procesando ID {appid}: {e}")
            time.sleep(5)

    with open(ARCHIVO_SALIDA, 'w', encoding='utf-8') as f:
        json.dump(biblioteca, f, ensure_ascii=False, indent=4)
    
    print(f"\n✨ ¡Misión cumplida! Datos guardados en '{ARCHIVO_SALIDA}'.")

if __name__ == "__main__":
    ejecutar_scrapper()