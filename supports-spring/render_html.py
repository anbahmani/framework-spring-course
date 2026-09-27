#!/usr/bin/env python3
"""Exporte le sous-ensemble Markdown des supports en HTML autonome, sans dépendance."""
from html import escape
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parent
UML_SEQUENCE_COUNTER = 0
STYLE = """:root{color-scheme:light;--ink:#172b3a;--accent:#087e72;--paper:#fff;--bg:#eff4f5}
*{box-sizing:border-box}body{margin:0;color:var(--ink);background:var(--bg);font:18px/1.65 system-ui,sans-serif}
header{background:#102f3c;color:white;padding:16px 5%;display:flex;gap:20px;align-items:center;flex-wrap:wrap}
header a{color:white}header span{flex:1}button{border:1px solid #b6dbd5;border-radius:6px;background:white;color:#123b3a;padding:8px 12px;cursor:pointer}
main{max-width:1120px;margin:28px auto;padding:0 24px}section{background:var(--paper);padding:38px 48px;margin-bottom:24px;border-radius:12px;border-top:4px solid var(--accent);box-shadow:0 2px 12px #16343a0a}
h1{font-size:2.1rem;line-height:1.2}h2{font-size:1.55rem;line-height:1.3;color:#086b63}h3{font-size:1.15rem}a{color:#076a87;text-underline-offset:3px}
p{margin:15px 0}li{margin:8px 0}code{font:0.87em ui-monospace,monospace;background:#edf3f5;padding:2px 4px;border-radius:4px}
pre{padding:20px;background:#102f3c;color:#edf8fa;overflow:auto;border-radius:8px;line-height:1.5;white-space:pre-wrap;overflow-wrap:anywhere}pre code{padding:0;background:none;color:inherit}
.table{overflow:auto}table{border-collapse:collapse;width:100%;font-size:.9em}th,td{text-align:left;vertical-align:top;padding:12px;border-bottom:1px solid #d8e3e5}th{background:#e5f0ee}
details{margin-top:26px;padding:18px;background:#edf7f4;border-radius:8px}summary{cursor:pointer;font-weight:700}footer{text-align:center;font-size:14px;padding:25px}
.diagram{margin:26px 0;padding:14px;background:#f8fbfb;border:1px solid #d8e8e7;border-radius:10px;overflow:auto}.diagram svg{display:block;max-width:100%;height:auto;margin:auto}.diagram .box{fill:#ffffff;stroke:#087e72;stroke-width:2}.diagram .arrow{stroke:#32515c;stroke-width:2.5;fill:none}.diagram text{font:15px system-ui,sans-serif;fill:#172b3a;text-anchor:middle}
.sequence-diagram .lifeline{stroke:#78909a;stroke-width:1.5;stroke-dasharray:6 6}.sequence-diagram .participant{fill:#e7f3f1;stroke:#087e72;stroke-width:2}.sequence-diagram .message{stroke:#32515c;stroke-width:2;fill:none}.sequence-diagram .message.return{stroke-dasharray:7 5}.sequence-diagram .message-label{font:14px system-ui,sans-serif;fill:#172b3a;text-anchor:middle}.sequence-diagram .actor-label{font:14px system-ui,sans-serif;fill:#172b3a;text-anchor:middle}
.deck-controls{display:none;gap:8px;align-items:center}.slide-count{min-width:4.5em;text-align:center;color:#e9fbf8}
body.projection section{min-height:75vh;font-size:1.12em}
body.deck main{max-width:1240px;margin:20px auto}body.deck section{display:none;min-height:calc(100vh - 150px);font-size:1.18em;margin-bottom:0}body.deck section.active{display:block}body.deck footer{display:none}body.deck .deck-controls{display:flex}body.hide-answers details{display:none}
@media(max-width:700px){main{padding:0 10px}section{padding:20px}h1{font-size:1.7rem}}
@media print{body{background:white;font-size:11pt}header,footer{display:none}main{max-width:none;margin:0;padding:0}section{box-shadow:none;border:0;padding:0;margin:0;break-after:page}section:last-child{break-after:auto}pre{background:#f1f4f5;color:black;font-size:9pt}h2,h3{break-after:avoid}tr,pre{break-inside:avoid}a{color:inherit}details{background:white}.table{overflow:visible}}
"""

def output_path(path):
    return path.with_name('index.html') if path.name == 'README.md' else path.with_suffix('.html')

def inline(value):
    # Protéger les fragments de code contre la mise en forme des astérisques.
    tokens = []
    def token(match):
        tokens.append('<code>' + escape(match.group(1)) + '</code>')
        return f'@@CODE{len(tokens)-1}@@'
    value = re.sub(r'`([^`]+)`', token, value)
    value = escape(value)
    def link(match):
        label, target = match.groups()
        if target.endswith('.md'):
            target = str(output_path(Path(target)))
        return f'<a href="{target}">{label}</a>'
    value = re.sub(r'\[([^\]]+)\]\(([^)]+)\)', link, value)
    value = re.sub(r'\*\*(.+?)\*\*', r'<strong>\1</strong>', value)
    for i, part in enumerate(tokens):
        value = value.replace(f'@@CODE{i}@@', part)
    return value

def split_label(label):
    words = label.strip().split()
    if len(label) <= 18 or len(words) == 1:
        return [label.strip()]
    first, second = [], []
    for word in words:
        target = first if len(' '.join(first + [word])) <= 18 else second
        target.append(word)
    return [' '.join(first), ' '.join(second)]

def render_diagram(code):
    cleaned = [line.strip() for line in code.splitlines() if line.strip()]
    if not cleaned:
        return ''
    nodes = []
    for line in cleaned:
        for part in line.split('->'):
            label = part.strip()
            if label and (not nodes or nodes[-1] != label):
                nodes.append(label)
    nodes = nodes[:6]
    box_w, box_h, gap, pad = 150, 76, 54, 22
    width = pad * 2 + len(nodes) * box_w + max(0, len(nodes) - 1) * gap
    height = 142
    parts = [f'<figure class="diagram"><svg role="img" viewBox="0 0 {width} {height}" aria-label="{escape(" puis ".join(nodes))}">',
             '<defs><marker id="arrow" markerWidth="10" markerHeight="10" refX="9" refY="3" orient="auto" markerUnits="strokeWidth"><path d="M0,0 L0,6 L9,3 z" fill="#32515c"/></marker></defs>']
    for i, node in enumerate(nodes):
        x, y = pad + i * (box_w + gap), 32
        parts.append(f'<rect class="box" x="{x}" y="{y}" width="{box_w}" height="{box_h}" rx="8"/>')
        label_lines = split_label(node)
        first_y = y + 40 - 10 * (len(label_lines) - 1)
        text = [f'<text x="{x + box_w / 2}" y="{first_y}">']
        for offset, label_line in enumerate(label_lines):
            text.append(f'<tspan x="{x + box_w / 2}" dy="{0 if offset == 0 else 20}">{escape(label_line)}</tspan>')
        text.append('</text>')
        parts.append(''.join(text))
        if i < len(nodes) - 1:
            start = x + box_w + 8
            end = x + box_w + gap - 8
            parts.append(f'<path class="arrow" d="M{start} {y + box_h / 2} H{end}" marker-end="url(#arrow)"/>')
    parts.append('</svg></figure>')
    return ''.join(parts)

def wrap_words(label, limit):
    lines, current = [], []
    for word in label.split():
        if current and len(' '.join(current + [word])) > limit:
            lines.append(' '.join(current)); current = []
        current.append(word)
    if current:
        lines.append(' '.join(current))
    return lines or ['']

def render_sequence_diagram(code):
    global UML_SEQUENCE_COUNTER
    UML_SEQUENCE_COUNTER += 1
    arrow_id = f'umlArrow{UML_SEQUENCE_COUNTER}'
    participants, messages = [], []
    for line in code.splitlines():
        line = line.strip()
        if not line:
            continue
        actor = re.fullmatch(r'participant\s+(\w+)(?:\s+as\s+(.+))?', line)
        message = re.fullmatch(r'(\w+)\s*(-->|->)\s*(\w+)\s*:\s*(.+)', line)
        if actor:
            participants.append((actor.group(1), (actor.group(2) or actor.group(1)).strip()))
        elif message:
            messages.append((message.group(1), message.group(2), message.group(3), message.group(4).strip()))
    ids = [actor_id for actor_id, _ in participants]
    if not participants or not messages or any(a not in ids or b not in ids for a, _, b, _ in messages):
        return '<p class="diagram-error">Diagramme de séquence invalide.</p>'

    margin, box_w, box_h, lane_gap = 34, 150, 48, 188
    width = margin * 2 + box_w + lane_gap * (len(participants) - 1)
    top, row_h, bottom = 30, 66, 32
    height = top + box_h + 22 + row_h * len(messages) + bottom
    centers = {actor_id: margin + box_w / 2 + i * lane_gap for i, (actor_id, _) in enumerate(participants)}
    description = '; '.join(f'{name}: {label}' for name, label in participants)
    parts = [f'<figure class="diagram sequence-diagram"><svg role="img" viewBox="0 0 {width} {height}" aria-label="Diagramme de séquence UML : {escape(description)}">',
             f'<defs><marker id="{arrow_id}" markerWidth="10" markerHeight="10" refX="9" refY="3" orient="auto"><path d="M0,0 L0,6 L9,3 z" fill="#32515c"/></marker></defs>']
    for actor_id, label in participants:
        x = centers[actor_id] - box_w / 2
        actor_lines = wrap_words(label, 20)
        first_y = top + box_h / 2 + 5 - 8 * (len(actor_lines) - 1)
        parts.append(f'<rect class="participant" x="{x}" y="{top}" width="{box_w}" height="{box_h}" rx="7"/>')
        parts.append(f'<text class="actor-label" x="{centers[actor_id]}" y="{first_y}">')
        for i, actor_line in enumerate(actor_lines):
            parts.append(f'<tspan x="{centers[actor_id]}" dy="{0 if i == 0 else 16}">{escape(actor_line)}</tspan>')
        parts.append('</text>')
        parts.append(f'<path class="lifeline" d="M{centers[actor_id]} {top + box_h} V{height - bottom}"/>')
    for row, (sender, arrow, receiver, label) in enumerate(messages):
        y = top + box_h + 34 + row * row_h
        x1, x2 = centers[sender], centers[receiver]
        start, end = (x1 + 5, x2 - 8) if x1 < x2 else (x1 - 5, x2 + 8)
        klass = 'message return' if arrow == '-->' else 'message'
        parts.append(f'<path class="{klass}" d="M{start} {y} H{end}" marker-end="url(#{arrow_id})"/>')
        label_lines = wrap_words(label, 34)
        first_y = y - 12 - 16 * (len(label_lines) - 1)
        parts.append(f'<text class="message-label" x="{(x1 + x2) / 2}" y="{first_y}">')
        for i, label_line in enumerate(label_lines):
            parts.append(f'<tspan x="{(x1 + x2) / 2}" dy="{0 if i == 0 else 17}">{escape(label_line)}</tspan>')
        parts.append('</text>')
    parts.append('</svg><figcaption>Diagramme de séquence UML</figcaption></figure>')
    return ''.join(parts)

def render(markdown):
    lines = markdown.splitlines()
    out = ['<section>']
    index = 0
    while index < len(lines):
        line = lines[index]
        if not line.strip():
            index += 1
            continue
        if line.startswith('```'):
            language = line[3:].strip()
            index += 1
            code = []
            while index < len(lines) and not lines[index].startswith('```'):
                code.append(lines[index]); index += 1
            if language == 'diagram':
                out.append(render_diagram('\n'.join(code)))
            elif language == 'uml-sequence':
                out.append(render_sequence_diagram('\n'.join(code)))
            else:
                out.append('<pre><code>' + escape('\n'.join(code)) + '</code></pre>')
        elif line == '---':
            out.append('</section><section>')
        elif re.match(r'^#{1,6} ', line):
            level = len(line.split(' ')[0])
            out.append(f'<h{level}>' + inline(line[level+1:]) + f'</h{level}>')
        elif line.startswith('|'):
            rows = []
            while index < len(lines) and lines[index].startswith('|'):
                cells = lines[index].strip('|').split('|')
                if not all(re.fullmatch(r'\s*:?-+:?\s*', cell) for cell in cells):
                    rows.append(cells)
                index += 1
            out.append('<div class="table"><table>')
            for row_index, cells in enumerate(rows):
                tag = 'th' if row_index == 0 else 'td'
                out.append('<tr>' + ''.join(f'<{tag}>'+inline(c.strip())+f'</{tag}>' for c in cells)+'</tr>')
            out.append('</table></div>'); continue
        elif re.match(r'^(\d+\. |[-*] )', line):
            numbered = line[0].isdigit()
            tag = 'ol' if numbered else 'ul'
            pattern = r'^\d+\. ' if numbered else r'^[-*] '
            out.append(f'<{tag}>')
            while index < len(lines) and re.match(pattern, lines[index]):
                out.append('<li>'+inline(re.sub(pattern, '', lines[index]))+'</li>')
                index += 1
            out.append(f'</{tag}>'); continue
        elif line in ('<details>', '</details>') or line.startswith('<summary>'):
            out.append(line)
        else:
            paragraph = [line]
            while index+1 < len(lines) and lines[index+1].strip() and not re.match(r'^(#|```|---|\||<details>|</details>|<summary>|\d+\. |[-*] )', lines[index+1]):
                index += 1; paragraph.append(lines[index])
            out.append('<p>'+inline(' '.join(paragraph))+'</p>')
        index += 1
    return '\n'.join(out) + '</section>'

def main():
    for source in sorted(ROOT.rglob('*.md')):
        if 'target' in source.parts:
            continue
        text = source.read_text()
        title = text.splitlines()[0].lstrip('# ')
        depth = len(source.relative_to(ROOT).parts)-1
        home = '../'*depth + 'index.html'
        body = render(text)
        page = f'''<!doctype html>
<html lang="fr"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>{escape(title)}</title><style>{STYLE}</style></head><body>
<header><a href="{home}">Architecture &amp; Spring</a><span>M1 MIAGE · Supports pédagogiques</span>
<button onclick="document.body.classList.toggle('projection')">Lecture / projection</button>
<span class="deck-controls"><button onclick="prevSlide()" aria-label="Diapositive précédente">◀</button><span class="slide-count" id="slideCount"></span><button onclick="nextSlide()" aria-label="Diapositive suivante">▶</button></span>
<button onclick="toggleDeck()">Diaporama</button>
<button onclick="document.body.classList.toggle('hide-answers')">Afficher / masquer les corrigés</button>
<button onclick="window.print()">Imprimer / PDF</button></header>
<main>{body}</main><footer>Source modifiable : <a href="{source.name}">{source.name}</a> · Ouvrir les corrigés avant impression pour les inclure.</footer>
<script>
const slides = Array.from(document.querySelectorAll('main section'));
let currentSlide = 0;
function showSlide(index) {{
  currentSlide = Math.max(0, Math.min(index, slides.length - 1));
  slides.forEach((slide, i) => slide.classList.toggle('active', i === currentSlide));
  const count = document.getElementById('slideCount');
  if (count) count.textContent = `${{currentSlide + 1}} / ${{slides.length}}`;
}}
function toggleDeck() {{
  document.body.classList.toggle('deck');
  showSlide(currentSlide);
}}
function nextSlide() {{ if (document.body.classList.contains('deck')) showSlide(currentSlide + 1); }}
function prevSlide() {{ if (document.body.classList.contains('deck')) showSlide(currentSlide - 1); }}
document.addEventListener('keydown', event => {{
  if (!document.body.classList.contains('deck')) return;
  if (['ArrowRight', 'PageDown', ' '].includes(event.key)) {{ event.preventDefault(); nextSlide(); }}
  if (['ArrowLeft', 'PageUp'].includes(event.key)) {{ event.preventDefault(); prevSlide(); }}
  if (event.key === 'Home') {{ event.preventDefault(); showSlide(0); }}
  if (event.key === 'End') {{ event.preventDefault(); showSlide(slides.length - 1); }}
}});
showSlide(0);
</script>
</body></html>'''
        output_path(source).write_text(page)
        print(output_path(source).relative_to(ROOT))

if __name__ == '__main__':
    main()
