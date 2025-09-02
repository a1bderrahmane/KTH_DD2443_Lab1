import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import ScalarFormatter

CSV = "Threads_stats.csv"  # <- put your file name here (the CSV you pasted)

# --- Load & prep ---
cols = ["n","X","Y","countX","countY","meanX","varianceX","meanY","varianceY"]
df = pd.read_csv(CSV, usecols=cols)
for c in cols:  # ensure numeric
    df[c] = pd.to_numeric(df[c], errors="coerce")
df = df.dropna().sort_values("n")
df["stdX"] = np.sqrt(df["varianceX"])
df["stdY"] = np.sqrt(df["varianceY"])

def sci(ax):
    ax.yaxis.set_major_formatter(ScalarFormatter(useMathText=True))
    ax.ticklabel_format(axis='y', style='sci', scilimits=(0, 0))

# --- meanX ± std vs n (scatter, no connecting line) ---
fig, ax = plt.subplots(figsize=(8,6), dpi=150)
ax.errorbar(df["n"], df["meanX"], yerr=df["stdX"], fmt='o', capsize=3, label="meanX ± std")
ax.set_title("MeanX vs n (with std)"); ax.set_xlabel("n"); ax.set_ylabel("meanX")
ax.grid(True); sci(ax); ax.legend()
fig.tight_layout(); fig.savefig("Threads_stats/meanX_vs_n.png")

# --- varianceX vs n (scatter, log scale helps) ---
fig, ax = plt.subplots(figsize=(8,6), dpi=150)
ax.scatter(df["n"], df["varianceX"])
ax.set_yscale("log")
ax.set_title("VarianceX vs n"); ax.set_xlabel("n"); ax.set_ylabel("varianceX (log scale)")
ax.grid(True, which="both"); sci(ax)
fig.tight_layout(); fig.savefig("Threads_stats/varianceX_vs_n.png")

# --- meanY ± std vs n ---
fig, ax = plt.subplots(figsize=(8,6), dpi=150)
ax.errorbar(df["n"], df["meanY"], yerr=df["stdY"], fmt='o', capsize=3, label="meanY ± std")
ax.set_title("MeanY vs n (with std)"); ax.set_xlabel("n"); ax.set_ylabel("meanY")
ax.grid(True); sci(ax); ax.legend()
fig.tight_layout(); fig.savefig("Threads_stats/meanY_vs_n.png")

# --- varianceY vs n ---
fig, ax = plt.subplots(figsize=(8,6), dpi=150)
ax.scatter(df["n"], df["varianceY"])
ax.set_yscale("log")
ax.set_title("VarianceY vs n"); ax.set_xlabel("n"); ax.set_ylabel("varianceY (log scale)")
ax.grid(True, which="both"); sci(ax)
fig.tight_layout(); fig.savefig("Threads_stats/varianceY_vs_n.png")
