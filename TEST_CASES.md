# Enhanced Greedy CPU AI - Test Cases

## Scoring Formula
```
score = (data_collected × 100) - cluster_distance
cluster_distance = avg Euclidean distance to 3 closest remaining data points
```

---

## Category 1: Cluster vs Isolated Data Advantage

### TC-CA-001: Dense Cluster vs Single Distant Data
**Grid (6×6):**
```
@ . . . . D
. . . . . .
. D D . . .
. D . . . .
. . . . . .
. . . . . H
```

**Setup:**
- Packet @ at (0,0)
- Data at: D1(5,0), D2(1,2), D3(2,2), D4(1,3)
- Cluster: D2,D3,D4 form tight group at ~(1.3, 2.3)

**Direction Analysis:**
- **East (→)**: Slides to (5,0), collects D1
  - Data collected: 1
  - Remaining: D2,D3,D4
  - Cluster dist: avg((2.24, 3.16, 4.24)) = 3.21
  - **Score: 100 - 3.21 = 96.79**

- **Southeast (↘)**: Slides to (1,2), collects D2
  - Data collected: 1
  - Remaining: D1,D3,D4
  - Cluster dist: avg((1.0, 1.41, 4.58)) = 2.33
  - **Score: 100 - 2.33 = 97.67** ✓

**Expected:**
- **New AI**: Chooses Southeast (97.67) - positions near remaining cluster
- **Old AI**: Would choose East (nearest single data)
- **Pass Criteria**: CPU moves Southeast, demonstrating cluster awareness

---

### TC-CA-002: Two Clusters - Near vs Far
**Grid (8×8):**
```
@ . D D . . . .
. . D . . . . .
. . . . . . . .
. . . . . . D D
. . . . . . D .
. . . . . . . .
. . . . . . . .
. . . . . . . H
```

**Setup:**
- Packet @ at (0,0)
- Cluster A: D1(2,0), D2(3,0), D3(2,1) at ~(2.3, 0.3)
- Cluster B: D4(6,3), D5(7,3), D6(6,4) at ~(6.3, 3.3)

**Direction Analysis:**
- **East**: Slides to (2,0), collects D1
  - Remaining: D2,D3 (near), D4,D5,D6 (far)
  - 3 closest: D2(1.0), D3(1.41), D4(6.08)
  - Cluster dist: 2.83
  - **Score: 100 - 2.83 = 97.17** ✓

**Expected:**
- **New AI**: Navigates strategically between clusters
- **Pass Criteria**: CPU evaluates both clusters and picks optimal positioning

---

### TC-CA-003: Multiple Data in Path
**Grid (6×6):**
```
@ D D D . .
. . . . . .
. . . . . .
. . . D D D
. . . . . .
. . . . . H
```

**Setup:**
- Packet @ at (0,0)
- Top row: D1(1,0), D2(2,0), D3(3,0)
- Bottom row: D4(3,3), D5(4,3), D6(5,3)

**Direction Analysis:**
- **East**: Slides to (3,0), collects D1,D2,D3 (3 data!)
  - Data collected: 3
  - Remaining: D4,D5,D6
  - Cluster dist to D4,D5,D6: avg((3.0, 4.0, 5.0)) = 4.0
  - **Score: 300 - 4.0 = 296.0** ✓

**Expected:**
- **New AI**: Chooses East - immediate collection outweighs positioning
- **Pass Criteria**: CPU correctly prioritizes multiple immediate data

---

## Category 2: Memoization Hits

### TC-MEM-001: Same End Position from Two Directions
**Grid (6×6):**
```
. . @ . . .
. . # # # .
. . # D # .
. . # # # .
. . . . . .
. . . . . H
```

**Setup:**
- Packet @ at (2,0)
- Data D at (3,2) (surrounded by firewalls)

**Memoization:**
- Multiple directions might simulate ending at same (x,y) with same remaining data
- **Pass Criteria**: 
  - Cluster distance calculated only once for duplicate positions
  - Cache hit logs in debug mode

---

### TC-MEM-002: Symmetric Board Layout
**Grid (6×6):**
```
. D . . D .
. . . . . .
@ . . . . .
. . . . . .
. . . . . .
. D . . D H
```

**Setup:**
- Packet @ at (0,2)
- Symmetric data: (1,0), (4,0), (1,5), (4,5)

**Memoization:**
- Different positions but potentially same data state
- **Pass Criteria**: Memo cache differentiates by position despite data symmetry

---

## Category 3: Edge Cases (0-2 Data Points Left)

### TC-EDGE-001: Single Data Point Remaining
**Grid (6×6):**
```
@ . . . . .
. . . . . .
. . . . D .
. . . . . .
. . . . . .
. . . . . H
```

**Setup:**
- Packet @ at (0,0)
- Only 1 data D at (4,2)

**Expected Behavior:**
- **Cluster distance**: With only 1 data point, use distance to that point
- **Southeast**: Ends at (4,2), collects D, Score = 100 ✓

**Pass Criteria:**
- CPU handles single-data case without array index errors
- Correctly navigates to collect last data point

---

### TC-EDGE-002: Two Data Points Remaining
**Grid (6×6):**
```
@ . . D . .
. . . . . .
. . . . . .
. D . . . .
. . . . . .
. . . . . H
```

**Setup:**
- Packet @ at (0,0)
- Two data: D1(3,0), D2(1,3)

**Expected Behavior:**
- **Cluster distance**: Avg of 2 closest (both used)
- **Pass Criteria**: Handles 2-data case, correct decision

---

### TC-EDGE-003: Zero Data Remaining
**Grid (6×6):**
```
@ . . . . .
. # . . . .
. # . . . .
. # . . . .
. # . . . .
. . . . . H
```

**Setup:**
- All data already collected
- Hub H at (5,5)

**Expected Behavior:**
- **Cluster distance**: No data left → cluster_dist = 0 (neutral)
- **Pass Criteria**: No divide-by-zero errors, valid move toward hub

---

## Category 4: Virus/Firewall Interference

### TC-SAFE-001: Cluster Beyond Virus
**Grid (6×6):**
```
@ . . V D D
. . . . D .
. . . . . .
. . . . . .
. . . . . .
. . . . . H
```

**Setup:**
- Packet @ at (0,0)
- Virus V at (3,0)
- Data cluster: D1(4,0), D2(5,0), D3(4,1)

**Expected:**
- **East**: Slides to (3,0) - HITS VIRUS ❌ (filtered out)
- **Pass Criteria**: CPU never chooses direction leading to virus

---

### TC-SAFE-002: Firewall Blocks Optimal Path
**Grid (8×8):**
```
@ # D D D D
. # . . . .
. # . . . .
. # . . . .
. . . . . .
. . . . . .
. . . . . .
. . . . . H
```

**Setup:**
- Firewall column # at x=1
- Dense data cluster at (2,0), (3,0), (4,0), (5,0)

**Expected:**
- **Pass Criteria**: Firewall collision detection prevents invalid moves

---

## Category 5: Blocking / No Valid Moves

### TC-BLOCK-001: Completely Surrounded
**Grid (6×6):**
```
. . . . . .
. # # # . .
. # @ # . .
. # # # . D
. . . . . .
. . . . . H
```

**Setup:**
- Packet @ at (2,2)
- Surrounded by firewalls

**Expected Behavior:**
- **All 8 directions**: Blocked by firewall
- **CPU Response**: Returns null (no valid move)
- **Pass Criteria**: No crash, graceful "no valid move" handling

---

### TC-BLOCK-002: All Directions Lead to Virus
**Grid (6×6):**
```
. V V V . .
V V @ V V D
. V V V . .
. . . . . .
. . . . . .
. . . . . H
```

**Setup:**
- Surrounded by viruses

**Expected Behavior:**
- **Pass Criteria**: CPU doesn't commit suicide, returns null gracefully

---

## Expected Improvements

| Scenario | Old Greedy | New Cluster-Aware | Improvement |
|----------|-----------|-------------------|-------------|
| Dense cluster | Picks nearest | Positions near cluster | +15% win rate |
| Multiple isolated | Random-ish | Strategic order | +20% efficiency |
| End-game (2-3 left) | Greedy only | Optimal positioning | +10% win rate |
| Complex boards | O(n²) calc | O(n log n) + memo | 2x faster |

**Total Test Cases**: 12 across 5 categories
