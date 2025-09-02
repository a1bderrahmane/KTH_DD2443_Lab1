import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import ScalarFormatter
from matplotlib.colors import LogNorm

CSV = "performance_stats.csv"  # change to your filename

# --- Load & ensure numeric ---
cols = ["X","Y","countX","countY","meanX","varianceX","meanY","varianceY"]
df = pd.read_csv(CSV, usecols=cols)
for c in cols:
    df[c] = pd.to_numeric(df[c], errors="coerce")
df = df.dropna().sort_values(["X","Y"]).reset_index(drop=True)

Xs = np.sort(df["X"].unique())
Ys = np.sort(df["Y"].unique())

def pivot(name):
    p = df.pivot(index="Y", columns="X", values=name)  # rows=Y, cols=X
    return p.reindex(index=Ys, columns=Xs)

def heatmap(piv, title, fname, log=False):
    fig, ax = plt.subplots(figsize=(8,6), dpi=150)
    data = piv.values
    if log:
        vmin = np.nanmin(data[data>0]) if np.any(data>0) else 1
        im = ax.imshow(data, origin="lower", aspect="auto",
                       norm=LogNorm(vmin=vmin, vmax=np.nanmax(data)))
    else:
        im = ax.imshow(data, origin="lower", aspect="auto")
    ax.set_title(title)
    ax.set_xlabel("X"); ax.set_ylabel("Y")
    ax.set_xticks(range(len(Xs))); ax.set_xticklabels(Xs, rotation=0)
    ax.set_yticks(range(len(Ys))); ax.set_yticklabels(Ys)
    cbar = fig.colorbar(im, ax=ax)
    cbar.formatter = ScalarFormatter(useMathText=True)
    cbar.formatter.set_powerlimits((0, 0))
    cbar.update_ticks()
    ax.grid(False)
    fig.tight_layout()
    fig.savefig(fname)
    plt.close(fig)

# --- Heatmaps ---
heatmap(pivot("meanX"),      "meanX over (X,Y)",      "X_Y_stats/heat_meanX.png", log=False)
heatmap(pivot("varianceX"),  "varianceX over (X,Y)",  "X_Y_stats/heat_varianceX.png", log=True)
heatmap(pivot("meanY"),      "meanY over (X,Y)",      "X_Y_stats/heat_meanY.png", log=False)
heatmap(pivot("varianceY"),  "varianceY over (X,Y)",  "X_Y_stats/heat_varianceY.png", log=True)

# --- Aggregations: mean across the other axis + error bars (std of means) ---
def sci(ax):
    ax.yaxis.set_major_formatter(ScalarFormatter(useMathText=True))
    ax.ticklabel_format(axis='y', style='sci', scilimits=(0,0))

# meanX vs X (aggregate over Y)
gX = df.groupby("X")["meanX"]
mX = gX.mean().reindex(Xs)
sX = gX.std(ddof=1).reindex(Xs)

fig, ax = plt.subplots(figsize=(8,6), dpi=150)
ax.errorbar(mX.index, mX.values, yerr=sX.values, fmt="o", capsize=3, label="meanX ± std over Y")
ax.set_title("meanX vs X (aggregated across Y)")
ax.set_xlabel("X"); ax.set_ylabel("meanX"); ax.grid(True); sci(ax); ax.legend()
fig.tight_layout(); fig.savefig("X_Y_stats/agg_meanX_vs_X.png"); plt.close(fig)

# meanY vs Y (aggregate over X)
gY = df.groupby("Y")["meanY"]
mY = gY.mean().reindex(Ys)
sY = gY.std(ddof=1).reindex(Ys)

fig, ax = plt.subplots(figsize=(8,6), dpi=150)
ax.errorbar(mY.index, mY.values, yerr=sY.values, fmt="o", capsize=3, label="meanY ± std over X")
ax.set_title("meanY vs Y (aggregated across X)")
ax.set_xlabel("Y"); ax.set_ylabel("meanY"); ax.grid(True); sci(ax); ax.legend()
fig.tight_layout(); fig.savefig("X_Y_stats/agg_meanY_vs_Y.png"); plt.close(fig)
