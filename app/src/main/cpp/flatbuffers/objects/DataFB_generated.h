// automatically generated by the FlatBuffers compiler, do not modify

#ifndef FLATBUFFERS_GENERATED_DATAFB_WATCHDOG_H_
#define FLATBUFFERS_GENERATED_DATAFB_WATCHDOG_H_

#include "../flatbuffers.h"

namespace watchdog {
struct SensorValueFB;
}  // namespace watchdog
namespace watchdog {
struct SampleFB;
}  // namespace watchdog

namespace watchdog {

struct DataFB;

struct DataFB FLATBUFFERS_FINAL_CLASS : private flatbuffers::Table {
  const flatbuffers::String *userLogin() const { return GetPointer<const flatbuffers::String *>(4); }
  const flatbuffers::Vector<flatbuffers::Offset<watchdog::SampleFB>> *samples() const { return GetPointer<const flatbuffers::Vector<flatbuffers::Offset<watchdog::SampleFB>> *>(6); }
  bool Verify(flatbuffers::Verifier &verifier) const {
    return VerifyTableStart(verifier) &&
           VerifyField<flatbuffers::uoffset_t>(verifier, 4 /* userLogin */) &&
           verifier.Verify(userLogin()) &&
           VerifyField<flatbuffers::uoffset_t>(verifier, 6 /* samples */) &&
           verifier.Verify(samples()) &&
           verifier.VerifyVectorOfTables(samples()) &&
           verifier.EndTable();
  }
};

struct DataFBBuilder {
  flatbuffers::FlatBufferBuilder &fbb_;
  flatbuffers::uoffset_t start_;
  void add_userLogin(flatbuffers::Offset<flatbuffers::String> userLogin) { fbb_.AddOffset(4, userLogin); }
  void add_samples(flatbuffers::Offset<flatbuffers::Vector<flatbuffers::Offset<watchdog::SampleFB>>> samples) { fbb_.AddOffset(6, samples); }
  DataFBBuilder(flatbuffers::FlatBufferBuilder &_fbb) : fbb_(_fbb) { start_ = fbb_.StartTable(); }
  DataFBBuilder &operator=(const DataFBBuilder &);
  flatbuffers::Offset<DataFB> Finish() {
    auto o = flatbuffers::Offset<DataFB>(fbb_.EndTable(start_, 2));
    return o;
  }
};

inline flatbuffers::Offset<DataFB> CreateDataFB(flatbuffers::FlatBufferBuilder &_fbb,
   flatbuffers::Offset<flatbuffers::String> userLogin = 0,
   flatbuffers::Offset<flatbuffers::Vector<flatbuffers::Offset<watchdog::SampleFB>>> samples = 0) {
  DataFBBuilder builder_(_fbb);
  builder_.add_samples(samples);
  builder_.add_userLogin(userLogin);
  return builder_.Finish();
}

inline const DataFB *GetDataFB(const void *buf) { return flatbuffers::GetRoot<DataFB>(buf); }

inline bool VerifyDataFBBuffer(flatbuffers::Verifier &verifier) { return verifier.VerifyBuffer<DataFB>(
            nullptr); }

inline void FinishDataFBBuffer(flatbuffers::FlatBufferBuilder &fbb, flatbuffers::Offset<DataFB> root) { fbb.Finish(root); }

}  // namespace watchdog

#endif  // FLATBUFFERS_GENERATED_DATAFB_WATCHDOG_H_
